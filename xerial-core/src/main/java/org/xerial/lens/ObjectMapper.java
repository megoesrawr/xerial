/*--------------------------------------------------------------------------
 *  Copyright 2009 Taro L. Saito
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
// XerialJ
//
// ObjectMapper.java
// Since: May 19, 2009 1:29:23 PM
//
// $URL$
// $Author$
//--------------------------------------
package org.xerial.lens;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.xerial.core.XerialError;
import org.xerial.core.XerialErrorCode;
import org.xerial.core.XerialException;
import org.xerial.lens.ParameterSetter.MapEntryBinder;
import org.xerial.relation.Node;
import org.xerial.relation.query.AmoebaJoinHandlerBase;
import org.xerial.relation.query.QuerySet;
import org.xerial.relation.query.StreamAmoebaJoin;
import org.xerial.relation.query.QuerySet.QuerySetBuilder;
import org.xerial.relation.schema.Schema;
import org.xerial.relation.schema.SchemaBuilder;
import org.xerial.util.ArrayDeque;
import org.xerial.util.Deque;
import org.xerial.util.bean.TypeConverter;
import org.xerial.util.bean.TypeInfo;
import org.xerial.util.log.Logger;
import org.xerial.util.tree.TreeWalker;

/**
 * Object-Tree mapping processor
 * 
 * @author leo
 * 
 */
public class ObjectMapper
{
    private static Logger _logger = Logger.getLogger(ObjectMapper.class);
    private HashMap<Long, Object> objectHolder = new HashMap<Long, Object>();

    private HashMap<Schema, Binder> schema2binder = new HashMap<Schema, Binder>();
    private Deque<Object> contextNodeStack = new ArrayDeque<Object>();

    private static interface Binder
    {
        public void bind(Schema schema, Node coreNode, Node attributeNode) throws XerialException;

        public void bindText(Schema schema, Node coreNode, String attributeValue) throws XerialException;

    }

    private class RelationBinder implements Binder
    {
        final ObjectLens lens;
        final RelationSetter setter;

        public RelationBinder(ObjectLens lens, RelationSetter setter)
        {
            this.lens = lens;
            this.setter = setter;
        }

        public void bind(Schema schema, Node coreNode, Node attributeNode) throws XerialException
        {
            Object coreNodeInstance = getNodeInstance(coreNode, setter.getCoreNodeType());
            Object attributeNodeInstance = getNodeInstance(attributeNode, setter.getAttributeNodeType());

            Object contextNode = findContextNode(lens.getTargetType());
            if (contextNode == null)
                throw new XerialError(XerialErrorCode.INVALID_INPUT, "no context node for " + setter);

            if (attributeNodeInstance != null)
                setter.bind(contextNode, coreNodeInstance, attributeNodeInstance);
        }

        public Object findContextNode(Class< ? > targetType)
        {
            for (Iterator<Object> it = contextNodeStack.descendingIterator(); it.hasNext();)
            {
                Object node = it.next();
                if (targetType.equals(node.getClass()))
                {
                    return node;
                }
            }
            return null;
        }

        public void bindText(Schema schema, Node coreNode, String attributeValue) throws XerialException
        {
            throw new XerialError(XerialErrorCode.UNSUPPORTED);
        }
    }

    private class AttributeBinder implements Binder
    {
        final ParameterSetter setter;
        final Class< ? > coreNodeType;
        final Class< ? > attributeNodeType;

        public AttributeBinder(Class< ? > coreNodeType, ParameterSetter setter)
        {
            this.setter = setter;
            this.coreNodeType = coreNodeType;
            this.attributeNodeType = setter.getParameterType();
        }

        public void bind(Schema schema, Node coreNode, Node attributeNode) throws XerialException
        {
            Object coreNodeInstance = getNodeInstance(coreNode, coreNodeType);
            Object attributeNodeInstance = getNodeInstance(attributeNode, attributeNodeType);

            if (attributeNodeInstance != null)
                setter.bind(coreNodeInstance, attributeNodeInstance);
        }

        public void bindText(Schema schema, Node coreNode, String textFragment) throws XerialException
        {
            Object coreNodeInstance = getNodeInstance(coreNode, coreNodeType);

            setter.bind(coreNodeInstance, textFragment);
        }
    }

    public ObjectMapper()
    {

    }

    public <T> T map(Class<T> targetType, TreeWalker walker) throws XerialException
    {
        T object = TypeInfo.createInstance(targetType);
        return map(object, walker);
    }

    public <T> T map(T object, TreeWalker walker) throws XerialException
    {
        try
        {
            if (object == null)
                throw new XerialError(XerialErrorCode.INVALID_INPUT, "null object");

            QuerySet qs = buildQuery(object.getClass());

            if (_logger.isDebugEnabled())
                _logger.debug("query set: " + qs);

            // set the root object
            objectHolder.put(0L, object);
            contextNodeStack.addLast(object);

            StreamAmoebaJoin aj = new StreamAmoebaJoin(qs, new RelationExtracter());
            aj.sweep(walker);
            return object;
        }
        catch (IOException e)
        {
            throw new XerialException(XerialErrorCode.IO_EXCEPTION, e);
        }

    }

    private QuerySet buildQuery(Class< ? > targetType)
    {
        QueryBuilder qb = new QueryBuilder();
        qb.build(targetType, "root");
        return qb.qs.build();
    }

    private class QueryBuilder
    {
        QuerySetBuilder qs = new QuerySetBuilder();
        private Set<Class< ? >> processedClasses = new HashSet<Class< ? >>();

        public QueryBuilder()
        {

        }

        public void build(Class< ? > targetType, String alias)
        {
            // TODO use context-based schema -> binder mapping

            if (TypeInfo.isBasicType(targetType) || targetType == MapEntry.class)
                return;

            if (processedClasses.contains(targetType))
                return;

            processedClasses.add(targetType);

            ObjectLens lens = ObjectLens.getObjectLens(targetType);
            if (_logger.isTraceEnabled())
                _logger.trace(String.format("class %s: %s\n", targetType.getSimpleName(), lens));

            for (ParameterSetter each : lens.getSetterList())
            {
                if (each.getClass() == MapEntryBinder.class)
                {
                    SchemaBuilder builder = new SchemaBuilder();
                    builder.add("entry");
                    builder.add(each.getParameterName());
                    Schema s = builder.build();
                    qs.addQueryTarget(s);
                    schema2binder.put(s, new AttributeBinder(MapEntry.class, each));
                    continue;
                }

                build(each.getParameterType(), each.getParameterName());

                SchemaBuilder builder = new SchemaBuilder();
                builder.add(alias);
                builder.add(each.getParameterName());

                Schema s = builder.build();
                qs.addQueryTarget(s);

                schema2binder.put(s, new AttributeBinder(lens.getTargetType(), each));
            }

            for (RelationSetter each : lens.getRelationSetterList())
            {
                build(each.getCoreNodeType(), each.getCoreNodeName());
                build(each.getAttributeNodeType(), each.getAttributeNodeName());

                Schema s = new SchemaBuilder().add(each.getCoreNodeName()).add(each.getAttributeNodeName()).build();
                qs.addQueryTarget(s);

                schema2binder.put(s, new RelationBinder(lens, each));

            }

        }

    }

    public Object getNodeInstance(Node node, Class< ? > nodeType) throws XerialException
    {
        Object instance = objectHolder.get(node.nodeID);
        if (instance != null)
            return instance;

        if (TypeInfo.isBasicType(nodeType))
        {
            if (node.nodeValue == null)
                return null;
            else
                instance = TypeConverter.convertToBasicType(nodeType, node.nodeValue);
        }
        else
        {
            instance = TypeInfo.createInstance(nodeType);
        }
        objectHolder.put(node.nodeID, instance);
        return instance;

    }

    private class RelationExtracter extends AmoebaJoinHandlerBase
    {

        public void leaveNode(Schema schema, Node node) throws Exception
        {
            Object obj = objectHolder.remove(node.nodeID);

            if (_logger.isTraceEnabled())
                _logger.trace(String.format("leave: %s in %s. object = %s", node, schema, obj));

        }

        public void newAmoeba(Schema schema, Node coreNode, Node attributeNode) throws Exception
        {
            if (_logger.isTraceEnabled())
                _logger.trace(String.format("amoeba: (%s, %s) in %s", coreNode, attributeNode, schema));

            Binder binder = schema2binder.get(schema);
            if (binder == null)
                throw new XerialError(XerialErrorCode.INVALID_STATE, "no binder for schema " + schema);

            binder.bind(schema, coreNode, attributeNode);

        }

        public void text(Schema schema, Node coreNode, String nodeName, String text) throws Exception
        {
            if (_logger.isTraceEnabled())
                _logger.trace(String.format("text:   (%s, %s:%s) in %s", coreNode, nodeName, text, schema));

            Binder binder = schema2binder.get(schema);
            if (binder == null)
                throw new XerialError(XerialErrorCode.INVALID_STATE, "no binder for schema " + schema);

            binder.bindText(schema, coreNode, text);
        }

    }

}

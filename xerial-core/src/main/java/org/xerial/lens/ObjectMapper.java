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
import org.xerial.lens.impl.MapEntry;
import org.xerial.lens.impl.ParameterSetter;
import org.xerial.lens.impl.RelationSetter;
import org.xerial.lens.impl.ParameterSetter.MapEntryBinder;
import org.xerial.lens.relation.Node;
import org.xerial.lens.relation.query.AmoebaJoinHandler;
import org.xerial.lens.relation.query.AmoebaJoinHandlerBase;
import org.xerial.lens.relation.query.QuerySet;
import org.xerial.lens.relation.query.StreamAmoebaJoin;
import org.xerial.lens.relation.query.QuerySet.QuerySetBuilder;
import org.xerial.lens.relation.schema.Schema;
import org.xerial.lens.relation.schema.SchemaBuilder;
import org.xerial.util.ArrayDeque;
import org.xerial.util.Deque;
import org.xerial.util.bean.TypeConverter;
import org.xerial.util.bean.TypeInfo;
import org.xerial.util.log.Logger;
import org.xerial.util.tree.TreeParser;

/**
 * Object-Tree mapping processor
 * 
 * @author leo
 * 
 */
public class ObjectMapper {
    private static Logger _logger = Logger.getLogger(ObjectMapper.class);

    // schema -> binder 
    private final HashMap<Schema, Binder> schema2binder = new HashMap<Schema, Binder>();

    private final QuerySet qs;

    private static HashMap<Class< ? >, ObjectMapper> prebuiltMapper = new HashMap<Class< ? >, ObjectMapper>();

    public <T> ObjectMapper(Class<T> targetType) throws XerialException {
        qs = buildQuery(targetType);
    }

    public static ObjectMapper getMapper(Class< ? > targetType) throws XerialException {
        if (prebuiltMapper.containsKey(targetType))
            return prebuiltMapper.get(targetType);
        else {
            ObjectMapper newInstance = new ObjectMapper(targetType);
            prebuiltMapper.put(targetType, newInstance);
            return newInstance;
        }
    }

    public <T> T map(Class<T> targetType, TreeParser parser) throws XerialException {

        T object = TypeInfo.createInstance(targetType);
        return map(object, parser);
    }

    public <T> T map(T object, TreeParser parser) throws XerialException {
        MappingProcess mp = new MappingProcess();
        return mp.execute(qs, object, parser);
    }

    private QuerySet buildQuery(Class< ? > targetType) {
        QueryBuilder qb = new QueryBuilder();
        qb.build(targetType, "root");
        return qb.qs.build();
    }

    /**
     * Build query components (schemas of two nodes) from the object lens
     * 
     * @author leo
     * 
     */
    private class QueryBuilder {
        QuerySetBuilder qs = new QuerySetBuilder();
        private final HashMap<String, Set<Class< ? >>> processedClassTable = new HashMap<String, Set<Class< ? >>>();

        //Set<Class< ? >> processedClasses = new HashSet<Class< ? >>();

        public QueryBuilder() {

        }

        public void build(Class< ? > targetType, String alias) {
            // TODO use context-based schema -> binder mapping

            if (TypeInfo.isBasicType(targetType) || targetType == MapEntry.class)
                return;

            Set<Class< ? >> processed = processedClassTable.get(alias);
            if (processed == null) {
                processed = new HashSet<Class< ? >>();
                processedClassTable.put(alias, processed);
            }

            if (processed.contains(targetType))
                return;
            else
                processed.add(targetType);

            //            if (processedClasses.contains(targetType))
            //                return;
            //
            //            processedClasses.add(targetType);

            ObjectLens lens = ObjectLens.getObjectLens(targetType);
            if (_logger.isTraceEnabled())
                _logger.trace(String.format("class %s: %s\n", targetType.getSimpleName(), lens));

            for (ParameterSetter each : lens.getSetterList()) {
                if (each.getClass() == MapEntryBinder.class) {
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

            for (RelationSetter each : lens.getRelationSetterList()) {
                build(each.getCoreNodeType(), each.getCoreNodeName());
                build(each.getAttributeNodeType(), each.getAttributeNodeName());

                Schema s = new SchemaBuilder().add(each.getCoreNodeName()).add(
                        each.getAttributeNodeName()).build();
                qs.addQueryTarget(s);

                schema2binder.put(s, new RelationBinder(lens, each));

            }

            // binding rule for the property setter

        }
    }

    /**
     * interface for invoking setters or field setters of the object
     * 
     * @author leo
     * 
     */
    private static interface Binder {
        public void bind(MappingProcess proc, Schema schema, Node coreNode, Node attributeNode)
                throws XerialException;

        public void bindText(MappingProcess proc, Schema schema, Node coreNode, Node textNode,
                String textValue) throws XerialException;

    }

    /**
     * Binds a node pair to the context node object. e.g. addA_B(A a, B b)
     * 
     * @author leo
     * 
     */
    private static class RelationBinder implements Binder {
        final ObjectLens lens;
        final RelationSetter setter;

        public RelationBinder(ObjectLens lens, RelationSetter setter) {
            this.lens = lens;
            this.setter = setter;
        }

        public void bind(MappingProcess proc, Schema schema, Node coreNode, Node attributeNode)
                throws XerialException {
            Object coreNodeInstance = proc.getNodeInstance(coreNode, setter.getCoreNodeType());
            Object attributeNodeInstance = proc.getNodeInstance(attributeNode, setter
                    .getAttributeNodeType());

            Object contextNode = findContextNode(proc, lens.getTargetType());
            if (contextNode == null)
                throw new XerialException(XerialErrorCode.INVALID_INPUT, "no context node for "
                        + setter);

            if (attributeNodeInstance != null) {
                setter.bind(contextNode, coreNodeInstance, attributeNodeInstance);
            }
        }

        public Object findContextNode(MappingProcess proc, Class< ? > targetType) {
            for (Iterator<Object> it = proc.contextNodeStack.descendingIterator(); it.hasNext();) {
                Object node = it.next();
                if (targetType.equals(node.getClass())) {
                    return node;
                }
            }
            return null;
        }

        public void bindText(MappingProcess proc, Schema schema, Node coreNode, Node textNode,
                String textValue) throws XerialException {
            throw new XerialError(XerialErrorCode.UNSUPPORTED);
        }
    }

    /**
     * 
     * Bind an attribute object to the context node (core node)
     * 
     * @author leo
     * 
     */
    private class AttributeBinder implements Binder {
        final ParameterSetter setter;
        final Class< ? > coreNodeType;
        final Class< ? > attributeNodeType;

        public AttributeBinder(Class< ? > coreNodeType, ParameterSetter setter) {
            this.setter = setter;
            this.coreNodeType = coreNodeType;
            this.attributeNodeType = setter.getParameterType();

        }

        public void bind(MappingProcess proc, Schema schema, Node coreNode, Node attributeNode)
                throws XerialException {
            Object coreNodeInstance = proc.getNodeInstance(coreNode, coreNodeType);
            Object attributeNodeInstance = proc.getNodeInstance(attributeNode, attributeNodeType);

            if (attributeNodeInstance != null)
                setter.bind(coreNodeInstance, attributeNodeInstance);
        }

        public void bindText(MappingProcess proc, Schema schema, Node coreNode, Node textNode,
                String textValue) throws XerialException {
            Object coreNodeInstance = proc.getNodeInstance(coreNode, coreNodeType);
            Object textNodeInstance = proc.getNodeInstance(textNode, attributeNodeType);

            if (textValue != null && !TypeInfo.isBasicType(attributeNodeType))
                proc.setTextValue(textNodeInstance, attributeNodeType, textValue);
            else
                setter.bind(coreNodeInstance, textValue);
        }
    }

    /**
     * MappingProcess performs mapping from tree event stream to objects.
     * 
     * 
     * @author leo
     * 
     */
    private class MappingProcess {
        // id -> corresponding object instance
        HashMap<Long, Object> objectHolder = new HashMap<Long, Object>();
        Deque<Object> contextNodeStack = new ArrayDeque<Object>();

        Object getNodeInstance(Node node, Class< ? > nodeType) throws XerialException {
            Object instance = objectHolder.get(node.nodeID);
            if (instance != null)
                return instance;

            if (TypeInfo.isBasicType(nodeType)) {
                if (node.nodeValue == null)
                    return null;
                else
                    instance = TypeConverter.convertToBasicType(nodeType, node.nodeValue);
            }
            else {
                instance = TypeInfo.createInstance(nodeType);

                if (node.nodeValue != null) {
                    setTextValue(instance, nodeType, node.nodeValue);
                }

            }
            objectHolder.put(node.nodeID, instance);
            return instance;
        }

        public <T> T execute(QuerySet qs, T object, TreeParser parser) throws XerialException {

            if (object == null)
                throw new XerialError(XerialErrorCode.INVALID_INPUT, "null object");

            if (_logger.isTraceEnabled())
                _logger.trace("query set: " + qs);

            // set the root object
            objectHolder.put(0L, object);
            contextNodeStack.addLast(object);

            try {
                AmoebaJoinHandler mapper = new RelationExtracter();

                StreamAmoebaJoin aj = new StreamAmoebaJoin(qs, mapper);
                aj.sweep(parser);
                return object;
            }
            catch (IOException e) {
                throw new XerialException(XerialErrorCode.IO_EXCEPTION, e);
            }
            catch (Exception e) {
                throw XerialException.convert(e);
            }

        }

        void setTextValue(Object instance, Class< ? > textNodeType, String textValue)
                throws XerialException {
            // bind the node value to the instance
            ObjectLens lens = ObjectLens.getObjectLens(textNodeType);
            ParameterSetter valueSetter = lens.getValueSetter();
            if (valueSetter != null)
                valueSetter.bind(instance, TypeConverter.convertToBasicType(valueSetter
                        .getParameterType(), textValue));

        }

        private Object getTextNodeInstance(String nodeName, String nodeValue, Class< ? > nodeType)
                throws XerialException {
            Object instance = null;

            if (TypeInfo.isBasicType(nodeType)) {
                if (nodeValue == null)
                    return null;
                else
                    instance = TypeConverter.convertToBasicType(nodeType, nodeValue);
            }
            else {
                instance = TypeInfo.createInstance(nodeType);

                if (nodeValue != null) {
                    // bind the node value to the instance
                    ObjectLens lens = ObjectLens.getObjectLens(nodeType);
                    ParameterSetter valueSetter = lens.getValueSetter();
                    if (valueSetter != null)
                        valueSetter.bind(instance, TypeConverter.convertToBasicType(valueSetter
                                .getParameterType(), nodeValue));
                }

            }

            return instance;
        }

        /**
         * Tree -> Relation -> Object binding process body
         * 
         * @author leo
         * 
         */
        private class RelationExtracter extends AmoebaJoinHandlerBase {

            @Override
            public void leaveNode(Schema schema, Node node) throws Exception {
                if (schema == null && node.nodeValue != null) {
                    // if putter is defined, set (node.nodeName, node.nodeValue) as (key, value)
                    if (_logger.isTraceEnabled())
                        _logger.trace("put: " + node);

                    Object contextNode = contextNodeStack.getLast();
                    ObjectLens lens = ObjectLens.getObjectLens(contextNode.getClass());
                    lens.setProperty(contextNode, node.getCanonicalNodeName(), node.nodeValue);
                }

                Object obj = objectHolder.remove(node.nodeID);

                if (_logger.isTraceEnabled())
                    _logger.trace(String.format("leave: %s in %s. object = %s", node, schema, obj));

            }

            @Override
            public void newAmoeba(Schema schema, Node coreNode, Node attributeNode)
                    throws Exception {
                if (_logger.isTraceEnabled())
                    _logger.trace(String.format("amoeba: (%s, %s) in %s", coreNode, attributeNode,
                            schema));

                Binder binder = schema2binder.get(schema);
                if (binder == null)
                    throw new XerialError(XerialErrorCode.INVALID_STATE, "no binder for schema "
                            + schema);

                try {
                    binder.bind(MappingProcess.this, schema, coreNode, attributeNode);
                }
                catch (XerialException e) {
                    _logger.warn(String.format(
                            "failed to bind: core node=%s, attribute node=%s, schema=%s\n%s",
                            coreNode, attributeNode, schema, e));
                }

            }

            @Override
            public void text(Schema schema, Node coreNode, Node textNode, String textFragment)
                    throws Exception {
                if (_logger.isTraceEnabled())
                    _logger.trace(String.format("text:   (%s, %s:%s) in %s", coreNode, textNode,
                            textFragment, schema));

                if (schema == null) {
                    // put(node name, text node value) if property setter exist 
                    Object contextNode = contextNodeStack.getLast();
                    ObjectLens lens = ObjectLens.getObjectLens(contextNode.getClass());
                    Object prevValue = lens.getProperty(contextNode, coreNode.nodeName);
                    String value = (prevValue == null) ? textFragment : prevValue.toString()
                            + textFragment;
                    lens.setProperty(contextNode, textNode.nodeName, value);
                    return;
                }

                Binder binder = schema2binder.get(schema);
                if (binder == null)
                    throw new XerialError(XerialErrorCode.INVALID_STATE, "no binder for schema "
                            + schema);

                try {
                    binder.bindText(MappingProcess.this, schema, coreNode, textNode, textFragment);
                }
                catch (XerialException e) {
                    _logger.warn(String.format(
                            "failed to bind text: core node=%s, attributeName=%s, text=%s\n%s",
                            coreNode, textNode, textFragment, e));
                }
            }

        }

    }

}

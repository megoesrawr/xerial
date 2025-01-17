/*--------------------------------------------------------------------------
 *  Copyright 2004 Taro L. Saito
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
// XerialJ Project
//
// InvertedPathTreeTest.java
// Since: 2005/06/02
//
// $URL$ 
// $Author$
//--------------------------------------
package org.xerial.xml.index;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.TestCase;

import org.xerial.core.XerialException;
import org.xerial.util.io.NullOutputStream;
import org.xerial.xml.XMLAttribute;
import org.xerial.xml.XMLException;
import org.xerial.xml.XMLGenerator;
import org.xerial.xml.index.InvertedPathTree;

public class InvertedPathTreeTest extends TestCase {
    public void testInvertedPathTree() throws IOException, XMLException, XerialException {
        // prepare XML data
        StringWriter xmlWriter = new StringWriter();
        XMLGenerator xout = new XMLGenerator(xmlWriter);
        xout.startTag("booklist");
        xout.startTag("book", new XMLAttribute().add("isbn", "20424142342")).startTag("author")
                .element("first-name", "Peter").element("last-name", "Buneman").endTag().element(
                        "author", new XMLAttribute().add("id", "3214"), "leoleo").element("year",
                        "2005").startTag("publisher").element("name", "Morgan-Kaufmann").endTag()
                .endTag();
        xout.startTag("book").startTag("authors").startTag("author").element("id", "4234").text(
                "taro").endTag().element("author", "yui").endTag().endTag();

        xout.endTag();
        xout.endDocument();
        xout.flush();

        Reader xmlSource = new BufferedReader(new StringReader(xmlWriter.getBuffer().toString()));

        InvertedPathTree ipt = new InvertedPathTree();
        ipt.generateFrom(xmlSource);
        ipt.outputGraphviz(new NullOutputStream());
    }
}

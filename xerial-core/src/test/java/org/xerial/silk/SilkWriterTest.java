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
// SilkWriterTest.java
// Since: Jul 28, 2009 12:08:53 PM
//
// $URL$
// $Author$
//--------------------------------------
package org.xerial.silk;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xerial.silk.SilkWriter.FormatConfig;
import org.xerial.util.FileResource;
import org.xerial.util.log.Logger;

public class SilkWriterTest {
    private static Logger _logger = Logger.getLogger(SilkWriterTest.class);

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void toSilk() throws Exception {
        StringWriter buf = new StringWriter();
        SilkWriter w = new SilkWriter(buf);

        FormatConfig fc = new FormatConfig();
        fc.insertSpaceAfterColon = true;
        w.setFormatConfig(fc);

        w.commentLine("utgb config format");
        SilkWriter config = w.node("config").attribute("version", "1.0");
        config.leaf("projectName", "utgb-shell");
        config.leaf("version", "2.0");
        config.leaf("group", "utgb");
        w.commentLine("database connection settings");

        SilkWriter dbNode = w.node("database").attribute("dbms", "sqlite").attribute("scope",
                "debug");
        dbNode.leaf("address", "db/sample.db");

        SilkWriter t = w.tabDataSchema("gene").attribute("id").attribute("start").attribute("end");
        t.dataLine("g1\t10\t20000");
        t.dataLine("g2\t1000\t30000");

        w.endDocument();

        String s = buf.toString();
        _logger.info(s);

        TreeWalkLog l1 = new TreeWalkLog();
        TreeWalkLog l2 = new TreeWalkLog();

        SilkWalker s1 = new SilkWalker(FileResource
                .open(SilkWriterTest.class, "writer-result.silk"));
        SilkWalker s2 = new SilkWalker(new StringReader(s));

        s1.walk(l1);
        s2.walk(l2);

        boolean cmp = TreeWalkLog.compare(l1, l2);
        assertTrue(cmp);
    }
}

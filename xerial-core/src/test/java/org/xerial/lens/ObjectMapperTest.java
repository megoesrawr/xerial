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
// ObjectMapperTest.java
// Since: May 19, 2009 1:33:50 PM
//
// $URL$
// $Author$
//--------------------------------------
package org.xerial.lens;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xerial.silk.SilkWalker;
import org.xerial.util.FileResource;
import org.xerial.util.log.Logger;

public class ObjectMapperTest
{
    private static Logger _logger = Logger.getLogger(ObjectMapperTest.class);

    @Before
    public void setUp() throws Exception
    {}

    @After
    public void tearDown() throws Exception
    {}

    public static class CoordinateData
    {
        public String group;
        public String name;
        public String species;
        public String revision;

        @Override
        public String toString()
        {
            return String.format("group=%s, name=%s, species=%s, revision=%s", group, name, species, revision);
        }

    }

    // query: (gene, name, start, end, strand)
    public static class GeneData
    {
        public int id;
        public String name;
        public long start;
        public long end;
        public String strand;

        private StringBuilder sequence = new StringBuilder();

        public void appendSequence(String seq)
        {
            sequence.append(seq);
        }

        @Override
        public String toString()
        {
            return String.format("id=%d, name=%s, start=%s, end=%s, strand=%s, sequence=%s", id, name, start, end,
                    strand, sequence.toString());
        }
    }

    public static class GeneDB
    {
        public final String description = null;

        public void addCoordinate_Gene(CoordinateData c, GeneData g)
        {
            _logger.debug(String.format("c(%s), g(%s)", c, g));
        }

        public void addCoordinate(CoordinateData c)
        {
            _logger.debug(c);
        }

    }

    @Test
    public void map() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        GeneDB gdb = mapper.map(GeneDB.class, new SilkWalker(FileResource.find(ObjectMapperTest.class, "gene.silk")));

        assertEquals("gene data", gdb.description);
    }
}
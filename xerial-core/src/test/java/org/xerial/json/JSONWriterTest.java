/*--------------------------------------------------------------------------
 *  Copyright 2008 Taro L. Saito
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
// JSONWriterTest.java
// Since: Feb 1, 2008 11:17:50 AM
//
// $URL$
// $Author$
//--------------------------------------
package org.xerial.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;
import org.xerial.util.bean.BeanException;
import org.xerial.util.bean.BeanUtil;
import org.xerial.util.log.Logger;

public class JSONWriterTest
{
    private static Logger _logger = Logger.getLogger(JSONWriterTest.class);

    @Test
    public void test() throws IOException, JSONException, BeanException
    {
        StringWriter writer = new StringWriter();
        JSONWriter json = new JSONWriter(writer);

        json.startObject();
        json.put("id", 1);
        json.put("name", "Leo");
        json.startArray("phone");
        json.add("xxx");
        json.add("yyy");

        json.endJSON();

        String jsonData = writer.toString();
        _logger.debug(jsonData);
        Person p = new Person();
        BeanUtil.populateBean(p, jsonData);

        assertEquals(1, p.getId());
        assertEquals("Leo", p.getName());
        assertEquals(2, p.getPhoneList().size());
        assertEquals("xxx", p.getPhoneList().get(0));
        assertEquals("yyy", p.getPhoneList().get(1));
    }

    @Test
    public void test2() throws IOException, JSONException, BeanException
    {
        StringWriter writer = new StringWriter();
        JSONWriter json = new JSONWriter(writer);

        json.startObject();
        json.put("id", 1);
        json.put("name", "Leo");
        json.startArray("phone");
        json.add("xxx");
        json.add("yyy");
        json.endArray();
        json.endObject();
        json.endJSON();

        String jsonData = writer.toString();
        _logger.debug(jsonData);
        Person p = new Person();
        BeanUtil.populateBean(p, jsonData);

        assertEquals(1, p.getId());
        assertEquals("Leo", p.getName());
        assertEquals(2, p.getPhoneList().size());
        assertEquals("xxx", p.getPhoneList().get(0));
        assertEquals("yyy", p.getPhoneList().get(1));
    }

    @Test
    public void testInvalidJSONData() throws IOException, JSONException, BeanException
    {
        StringWriter writer = new StringWriter();
        JSONWriter json = new JSONWriter(writer);

        json.startObject();
        json.put("id", 1);
        json.put("name", "Leo");
        json.startArray("phone");
        json.add("xxx");
        json.add("yyy");

        try
        {
            json.endObject();
        }
        catch (JSONException e)
        {
            assertEquals(JSONErrorCode.NotInAJSONObject, e.getErrorCode());
            return;
        }

        fail("cannot reach here");
        json.endJSON();
    }

}

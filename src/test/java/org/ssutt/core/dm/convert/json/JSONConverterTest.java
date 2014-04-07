/*
 * Copyright 2014 Plain Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ssutt.core.dm.convert.json;

import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.ssutt.core.dm.convert.json.entity.DepartmentEntity;
import org.ssutt.core.dm.convert.json.serializer.DepartmentSerializer;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class JSONConverterTest {

    @Test
    public void testConvertDepartmentList() throws Exception {
        Map<String, Map<String, String>> departments = new LinkedHashMap<>();

        String expected = "{\"bf\":{\"name\":\"Biology\"}," +
                "\"gf\":{\"name\":\"Geography\"}," +
                "\"knt\":{\"name\":\"CSIT\"}}";

        String[] departmentCodes = {"bf", "gf", "knt"};
        String[][] departmentInfo = {{"name", "Biology"}, {"name", "Geography"}, {"name", "CSIT"}};

        for (int i = 0; i < 3; i++) {
            Map<String, String> info = new LinkedHashMap<>();
            info.put(departmentInfo[i][0], departmentInfo[i][1]);
            departments.put(departmentCodes[i], info);
        }

        GsonBuilder gsb = new GsonBuilder();
        gsb.registerTypeAdapter(DepartmentEntity.class, new DepartmentSerializer());

        String result = gsb.create().toJson(departments);

        System.out.println("expected: " + expected);
        System.out.println("result: " + result);

        assertEquals(expected, result);
    }

    @Test
    public void testConvertGroupList() throws Exception {

    }

    @Test
    public void testConvertAbstractList() throws Exception {

    }

    @Test
    public void testConvertGroupName() throws Exception {

    }

    @Test
    public void testConvertTT() throws Exception {

    }

    @Test
    public void testConvertStatus() throws Exception {

    }

    @Test
    public void testConvertStatus1() throws Exception {

    }

    @Test
    public void testReverseConvertGroup() throws Exception {

    }
}

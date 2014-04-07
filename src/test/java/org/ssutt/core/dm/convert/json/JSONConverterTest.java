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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.junit.Test;
import org.ssutt.core.dm.TTStatus;
import org.ssutt.core.dm.convert.json.entity.DepartmentEntity;
import org.ssutt.core.dm.convert.json.entity.StatusEntity;
import org.ssutt.core.dm.convert.json.entity.TimeTableEntity;
import org.ssutt.core.dm.convert.json.serializer.DepartmentSerializer;
import org.ssutt.core.dm.convert.json.serializer.TimeTableSerializer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

        assertEquals(expected, result);
    }

    @Test
    public void testConvertGroupList() throws Exception {
        List<String> groups = new ArrayList<>();

        String expected = "[\"111\",\"123\",\"145\",\"String group\"]";

        groups = Arrays.asList(new String[]{"111", "123", "145", "String group"});

        Gson gson = new Gson();

        String result = gson.toJson(groups);

        assertEquals(expected, result);

    }

    @Test
    public void testConvertTT() throws Exception {
        GsonBuilder gsb = new GsonBuilder();

        List<String[]> tt = new ArrayList<>();

        String[] t0 = new String[]{"mon", "even", "1", "calc"};
        String[] t1 = new String[]{"tue", "odd", "2", "code"};
        String[] t2 = new String[]{"wed", "all", "3", "la"};

        tt.add(t0);
        tt.add(t1);
        tt.add(t2);

        String expected = "{\"mon\":[{\"parity\":\"even\",\"sequence\":\"1\",\"info\":\"calc\"}]," +
                "\"tue\":[{\"parity\":\"odd\",\"sequence\":\"2\",\"info\":\"code\"}]," +
                "\"wed\":[{\"parity\":\"all\",\"sequence\":\"3\",\"info\":\"la\"}]}";

        Map<String, List<Map<String, String>>> temp = new LinkedHashMap<>();

        for (String[] record : tt) {
            String weekday = record[0];
            Map<String, String> t = new LinkedHashMap<>();
            t.put("parity", record[1]);
            t.put("sequence", record[2]);
            t.put("info", record[3]);
            if (temp.containsKey(weekday)) {
                temp.get(weekday).add(t);
            } else {
                List<Map<String, String>> tT = new ArrayList<>();
                tT.add(t);
                temp.put(weekday, tT);
            }
        }

        gsb.registerTypeAdapter(TimeTableEntity.class, new TimeTableSerializer());

        String result = gsb.create().toJson(temp);

        assertEquals(expected, result);

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

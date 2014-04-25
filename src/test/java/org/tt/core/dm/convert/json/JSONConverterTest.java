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

package org.tt.core.dm.convert.json;


import org.junit.Test;
import org.tt.core.dm.TTStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class JSONConverterTest {
    private JSONConverter jsc = new JSONConverter();

    //@Test
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

       // String result = jsc.convertDepartmentList(departments);
        //assertEquals(expected, result);
    }

    @Test
    public void testConvertGroupList() throws Exception {
        String expected = "[\"111\",\"123\",\"145\",\"String group\"]";
        List<String> groups = Arrays.asList("111", "123", "145", "String group");

    //    String result = jsc.convertGroupList(groups);
      //  assertEquals(expected, result);
    }

    @Test
    public void testConvertTT() throws Exception {
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

        //String result = jsc.convertTT(tt);
        //assertEquals(expected, result);
    }

    @Test
    public void testConvertStatus() throws Exception {
        String expected = "{\"module\":\"IO\",\"message\":\"URL Exception\"}";

        String result = jsc.convertStatus(TTStatus.IO, TTStatus.IOERR);
        assertEquals(expected, result);
    }

}

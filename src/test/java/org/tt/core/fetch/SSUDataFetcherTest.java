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
package org.tt.core.fetch;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SSUDataFetcherTest {
    private static AbstractDataFetcher df;

    @Before
    public void initializeDataFetcher() throws MalformedURLException {
        df = new SSUDataFetcher();
    }

    @Test
    public void testGetDepartments() throws Exception {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("Biology", "bf");
        expected.put("Geography", "gf");

        String html;
        try (BufferedReader br = new BufferedReader(new FileReader("./src/test/resources/departments.html"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            html = sb.toString();
        }
        Map<String, String> result = df.getDepartments(df.fetch(html, true));

        boolean status = true;
        status = result.keySet().containsAll(expected.keySet());

        for (String s : result.keySet()) {
            if (result.get(s) == expected.get(s)) {
                status = false;
                break;
            }
        }

        assertTrue(status);
    }

    @Test
    public void testGetGroups() throws Exception {
        List<String> expected = new LinkedList<>(Arrays.asList(new String[]{"442", "461"}));

        String html;
        try (BufferedReader br = new BufferedReader(new FileReader("./src/test/resources/groups.html"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            html = sb.toString();
        }
        List<String> result = df.getGroups(df.fetch(html, true), "bf");

        assertEquals(expected, result);
    }

    @Test
    public void testGetTT() throws Exception {
        String expected = "лек.  Математика Купцов С. Н. 9 корп. ауд. 201пр.  " +
                "Практический курс иностранного языка перев. 1 Алексеева Д. А. 18 корп. ауд. 113 пр.  " +
                "Практический курс иностранного языка перев. 3 Иголкина Н. И. 9 корп. ауд. 207 пр.  " +
                "Практический курс иностранного языка перев. 2 Сосновская А. А. 9 корп. ауд. 205 пр.  " +
                "Иностранный язык (английский) англ. 7 Смирнова А. Ю. 9 корп. ауд. 204";
        String html;
        try (BufferedReader br = new BufferedReader(new FileReader("./src/test/resources/timetable.html"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            html = sb.toString();
        }
        String[][] result = df.getTT(df.fetch(html, true));
        StringBuilder sb = new StringBuilder();
        for (String[] day : result) {
            for (String lesson : day)
                sb.append(lesson);
        }

        assertEquals(expected, sb.toString());

    }
}

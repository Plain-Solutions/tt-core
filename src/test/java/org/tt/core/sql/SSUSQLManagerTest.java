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
package org.tt.core.sql;


import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SSUSQLManagerTest {
    private static AbstractSQLManager sqlm;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Before
    public void initializeTestDB() throws SQLException, ClassNotFoundException {
        if (sqlm == null) {
            Class.forName("org.h2.Driver");
            Connection c = DriverManager.
                    getConnection("jdbc:h2:mem:localtest;INIT=RUNSCRIPT FROM './src/test/resources/initTT.sql'", "sa", "");

            sqlm = new SSUSQLManager(c);
            sqlm.setQueries(new H2Queries());
        }
    }

    @Test
    public void aTestPutDepartemnts() throws Exception {
        Map<String, String> departments = new LinkedHashMap<>();
        departments.put("Biology", "bf");
        departments.put("Geography", "gf");

        sqlm.putDepartments(departments);
    }


    @Test
    public void bTestPutGroups() throws Exception {
        List<String> bfgroups = Arrays.asList("111", "222", "111");
        List<String> gfgroups = Arrays.asList("121", "StringGroup");

        sqlm.putGroups(bfgroups, "bf");
        sqlm.putGroups(gfgroups, "gf");
    }


    @Test
    public void cTestPutDateTime() throws Exception {
        sqlm.putDateTime(1, 1, 3);
        sqlm.putDateTime(2, 2, 3);
    }

    @Test
    public void dTestPutSubject() throws Exception {
        sqlm.putSubject("algebra");
        sqlm.putSubject("physics");
    }

    @Test
    public void eTestPutLessonRecord() throws Exception {
        sqlm.putLessonRecord(1, 1, 1);
        sqlm.putLessonRecord(2, 2, 2);
        sqlm.putLessonRecord(4, 2, 1);
    }

    @Test
    public void fTestGetDepartments() throws Exception {
        Map<String, Map<String, String>> expected = new LinkedHashMap<>();

        Map<String, String> biologydata = new HashMap<>();
        biologydata.put("name", "Biology");

        Map<String, String> geographydata = new HashMap<>();
        geographydata.put("name", "Geography");

        expected.put("bf", biologydata);
        expected.put("gf", geographydata);

        Map<String, Map<String, String>> result = sqlm.getDepartments();
        assertEquals(expected, result);
    }

    @Test
    public void gTestGetDepartmentTags() throws Exception {
        List<String> expected = Arrays.asList("bf", "gf");
        List<String> result = sqlm.getDepartmentTags();

        assertEquals(expected, result);
    }

    @Test
    public void hTestGetGroups() throws Exception {
        List<String> expected = new LinkedList<>(Arrays.asList("111", "222"));
        List<String> result = sqlm.getGroups("bf");

        assertEquals(expected, result);

        result.clear();
        expected.clear();

        expected = new LinkedList<>(Arrays.asList("121", "StringGroup"));
        result = sqlm.getGroups("gf");
        assertEquals(expected, result);
    }

    @Test
    public void iTestGetGroupID() throws Exception {
        int expected = 4;
        int result = sqlm.getGroupID("gf", "StringGroup");

        assertEquals(expected, result);
    }

    @Test
    public void jTestGetGroupName() throws Exception {
        String expected = "StringGroup";
        String result = sqlm.getGroupName("gf", 4);

        assertEquals(expected, result);
    }

    @Test
    public void kTestGetTT() throws Exception {
        String[] expected = new String[]{"wed", "even", "1", "algebra"};
        List<String[]> result = sqlm.getTT(1);
        Comparator<String[]> compareStringArrays = new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                int res = -1;

                for (int i = 0; i < o1.length; i++) {
                    res = o1[i].compareTo(o2[i]);
                }

                return res;
            }
        };

        boolean status = false;


        for (String[] elem: result) {
            if (compareStringArrays.compare(expected, elem) == 0) {
                status = true;
            }
        }


        assertTrue(status);
    }

    @Test
    public void lTestDepartmentExists() throws Exception {
        boolean result = sqlm.departmentExists("gf");
        assertTrue(result);

        result = sqlm.departmentExists("nonex");
        assertFalse(result);
    }

    @Test
    public void mTestGroupExistsInDepartment() throws Exception {
        boolean result = sqlm.groupExistsInDepartment("bf", "111");
        assertTrue(result);

        result = sqlm.groupExistsInDepartment("bf", "999");
        assertFalse(result);
    }

    @Test
    public void nTestGroupExistsAsID() throws Exception {
        boolean result = sqlm.groupExistsAsID(4);
        assertTrue(result);

        result = sqlm.groupExistsAsID(5);
        assertFalse(result);
    }

    @Test
    public void oTestLessonExists() throws Exception {
        boolean result = sqlm.lessonExists(1, 1, 1);
        assertTrue(result);

        result = sqlm.lessonExists(5, 1, 2);
        assertFalse(result);
    }

    @Test
    public void pTestGetLastID() throws Exception {
        int expected = 4;

        int result = sqlm.getLastID("groups");
        assertEquals(expected, result);
    }
}

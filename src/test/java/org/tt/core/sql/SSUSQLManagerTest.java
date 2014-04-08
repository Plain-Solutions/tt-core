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
import java.util.HashMap;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SSUSQLManagerTest {
    private static AbstractSQLManager sqlm;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Before
    public void initializeTestDB() throws SQLException, ClassNotFoundException {
            if (sqlm==null) {
                Class.forName("org.h2.Driver");
                Connection c = DriverManager.
                        getConnection("jdbc:h2:mem:localtest;INIT=RUNSCRIPT FROM './src/test/resources/initTT.sql'", "sa", "");

                sqlm = new SSUSQLManager(c);

                sqlm.setQueries(new H2Queries());
            }
    }

    @Test
    public void a_dbPrepare() throws Exception {
        Map<String, String> departments = new LinkedHashMap<>();
        departments.put("Biology", "bf");
        departments.put("Geography", "gf");

        sqlm.putDepartments(departments);
    }


    @Test
    public void b_testPutGroups() throws Exception {
        List<String> bfgroups = Arrays.asList(new String[]{"111", "222"});
        List<String> gfgroups = Arrays.asList(new String[]{"121","StringGroup"});

        sqlm.putGroups(bfgroups, "bf");
        sqlm.putGroups(gfgroups, "gf");
    }



    @Test
    public void c_testPutDateTime() throws Exception {
        sqlm.putDateTime(1,1,3);
        sqlm.putDateTime(2,2,3);
    }

    @Test
    public void d_testPutSubject() throws Exception {
        sqlm.putSubject("algebra");
        sqlm.putSubject("physics");
    }

    @Test
    public void e_testPutLessonRecord() throws Exception {
        sqlm.putLessonRecord(1,1,1);
        sqlm.putLessonRecord(2,2,2);
        sqlm.putLessonRecord(4,2,1);
    }

    @Test
    public void f_testGetDepartments() throws Exception {
        Map <String, Map<String, String>> expected = new LinkedHashMap<>();

        Map <String, String> biologydata = new HashMap<>();
        biologydata.put("name", "Biology");

        Map <String, String> geographydata = new HashMap<>();
        geographydata.put("name", "Geography");

        expected.put("bf", biologydata);
        expected.put("gf", geographydata);

        Map<String, Map<String, String>> result = sqlm.getDepartments();

        assertEquals(expected, result);
    }

    @Test
    public void testGetDepartmentTags() throws Exception {

    }

    @Test
    public void testGetGroups() throws Exception {

    }

    @Test
    public void testGetGroupID() throws Exception {

    }

    @Test
    public void testGetGroupName() throws Exception {

    }

    @Test
    public void testGetTT() throws Exception {

    }

    @Test
    public void testDepartmentExists() throws Exception {

    }

    @Test
    public void testGroupExistsInDepartment() throws Exception {

    }

    @Test
    public void testGroupExistsAsID() throws Exception {

    }

    @Test
    public void testLessonExists() throws Exception {

    }

    @Test
    public void testGetLastID() throws Exception {

    }
}

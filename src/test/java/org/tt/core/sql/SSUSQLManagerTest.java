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

import org.junit.Test;
import org.tt.core.entity.datafetcher.Department;
import org.tt.core.entity.datafetcher.Group;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SSUSQLManagerTest {

    @Test
    public void testUpdateDepartmentMessage() throws Exception {

    }

    @Test
    public void testUpdateDepartmentInfo() throws Exception {

    }

    @Test
    public void testGetDepartments() throws Exception {
        AbstractSQLManager sqlm = SQLMTestWrapper.produceNewManager();
        List<Department> result = sqlm.getDepartments();
        List<Department> expected = java.util.Arrays.asList(new Department("Biology House", "bf", "Crucial Information!"),
                new Department("Geography House", "gf", ""));

        assertTrue("Departments getting failed" +
                        "\n  'result'        = " + result +
                        "\n  'expected' = " + expected,
                expected.equals(result)
        );

    }

    @Test
    public void testGetDepartmentTags() throws Exception {
        AbstractSQLManager sqlm = SQLMTestWrapper.produceNewManager();
        List<String> result = sqlm.getDepartmentTags();
        List<String> expected = Arrays.asList("bf", "gf");

        assertTrue("Department tags getting failed" +
                        "\n  'result'        = " + result +
                        "\n  'expected' = " + expected,
                expected.equals(result)
        );

    }

    @Test
    public void testGetNonEmptyDepartmentMessage() throws Exception {
        AbstractSQLManager sqlm = SQLMTestWrapper.produceNewManager();
        String result = sqlm.getDepartmentMessage("bf");
        String expected = "Crucial Information!";

        assertEquals("Department message getting failed.", expected, result);
    }

    @Test
    public void testGetEmptyDepartmentMessage() throws Exception {
        AbstractSQLManager sqlm = SQLMTestWrapper.produceNewManager();
        String result = sqlm.getDepartmentMessage("gf");
        String expected = "";

        assertEquals("Department (empty) message getting failed.", expected, result);
    }


   @Test
    public void testGetGroups() throws Exception {
        AbstractSQLManager sqlm = SQLMTestWrapper.produceNewManager();
        List<Group> result = sqlm.getGroups("bf");
        List<Group> expected = Arrays.asList(new Group("111"), new Group("String name"));

        assertTrue("Groups (bf, not gf) getting failed" +
                        "\n  'result'        = " + result +
                        "\n  'expected' = " + expected,
                expected.equals(result)
        );


    }

    @Test
    public void testGetNonEmptyGroups() throws Exception {

    }

    @Test
    public void testGetTT() throws Exception {

    }

    @Test
    public void testGetLessonList() throws Exception {

    }

    @Test
    public void testDeleteDepartment() throws Exception {

    }

    @Test
    public void testDeleteGroupFromDepartment() throws Exception {

    }

    @Test
    public void testDeleteLessons() throws Exception {

    }

    @Test
    public void testDeleteSubGroups() throws Exception {

    }

    @Test
    public void testDeleteLesson() throws Exception {

    }

    @Test
    public void testFlushDatabase() throws Exception {

    }
}

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
import org.tt.core.entity.datafetcher.Lesson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SSUSQLManagerTest {

    @Test
    public void testUpdateDepartmentMessage() throws Exception {
        AbstractSQLManager sqlm = SQLMTestWrapper.produceNewManager();
        sqlm.updateDepartmentMessage("gf", "Add info");

        assertEquals("Message update failed", "Add info", sqlm.getDepartmentMessage("gf"));
    }

    @Test
    public void testUpdateDepartmentInfo() throws Exception {
        AbstractSQLManager sqlm = SQLMTestWrapper.produceNewManager();
        sqlm.updateDepartmentInfo("CS&IT", "knt", "Test", "bf");

        Department result = new Department();
        for (Department d : sqlm.getDepartments()) {
            if (d.getTag().equals("knt")) {
                result = d;
            }
        }

        assertEquals("Department update failed", new Department("CS&IT", "knt", "Test"), result);

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
        AbstractSQLManager sqlm = SQLMTestWrapper.produceNewManager();
        List<Group> result = sqlm.getNonEmptyGroups("bf");
        List<Group> expected = Arrays.asList(new Group("111"));

        assertTrue("Non-empty groups getting failed" +
                        "\n  'result'        = " + result +
                        "\n  'expected' = " + expected,
                expected.equals(result)
        );

    }

    @Test
    public void testGetLessonList() throws Exception {
        //getTT is equal to it, but TTEntity has no compare algorithm.
        AbstractSQLManager sqlm = SQLMTestWrapper.produceNewManager();
        List<List<Lesson>> result = sqlm.getLessonList(sqlm.getGroupID("bf", "111"));

        List<List<Lesson>> expected = new ArrayList<>(6);

        while (expected.size() <= 6) expected.add(new ArrayList<Lesson>());

        List<Lesson> day = new ArrayList<>();

        Lesson aLesson = new Lesson();
        aLesson.setParity("nom");
        aLesson.setSequence(1);
        aLesson.setSubgroup("1st");
        aLesson.setSubject("Calculus");
        aLesson.setActivity("lecture");
        aLesson.setBuilding("B1");
        aLesson.setRoom("Default");
        aLesson.setTeacher("Sakhno L.V.");
        aLesson.setTimestamp(12345);

        day.add(aLesson);
        expected.set(0, day);

        boolean flag = true;

        //check with day order
        for (int i = 0; i < 6; i++) {
            for (Lesson l : expected.get(i)) {
                if (!(result.get(i).contains(l))) {
                    flag = false;
                }
            }
        }


        assertTrue("Getting TT failed", flag);

    }

    @Test
    public void testDeleteDepartment() throws Exception {
        AbstractSQLManager sqlm =SQLMTestWrapper.produceNewManager();
        sqlm.deleteDepartment(new Department("Geography House", "gf", ""));

        List<Department> result = sqlm.getDepartments();
        List<Department> expected = java.util.Arrays.asList(new Department("Biology House", "bf", "Crucial Information!"));

        assertTrue("Departments removing failed" +
                        "\n  'result'        = " + result +
                        "\n  'expected' = " + expected,
                expected.equals(result)
        );

    }

    @Test
    public void testDeleteGroupFromDepartment() throws Exception {
        AbstractSQLManager sqlm =SQLMTestWrapper.produceNewManager();
        sqlm.deleteGroupFromDepartment(new Department("Biology House", "bf", "Crucial Information!"),
                new Group("111"));

        List<Group> resultOne = sqlm.getGroups("bf");
        List<Group> resultTwo = sqlm.getNonEmptyGroups("bf");

        List<Group> expectedOne = Arrays.asList(new Group("String name"));
        List<Group> expectedTwo = new ArrayList<>();

        assertTrue("Groups removing failed: record about group not removed" +
                        "\n  'result'        = " + resultOne +
                        "\n  'expected' = " + expectedOne,
                expectedOne.equals(resultOne)
        );

        assertTrue("Groups removing failed: group data (lessons, subgroups) not removed" +
                        "\n  'result'        = " + resultTwo +
                        "\n  'expected' = " + expectedTwo,
                expectedTwo.equals(resultTwo)
        );

    }
}

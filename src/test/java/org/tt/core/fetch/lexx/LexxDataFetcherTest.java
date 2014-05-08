package org.tt.core.fetch.lexx;

import org.junit.Test;
import org.tt.core.entity.datafetcher.Department;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by fau on 07/05/14.
 */
public class LexxDataFetcherTest {
    @Test
    public void testGetDepartmentByTags() throws Exception {
        LDFTestWrapper ldf = new LDFTestWrapper();
        List<Department> result = ldf.getDepartments();

        String[] expected = {"bf","gf"};

        List<String> actualTags = new ArrayList<>();
        for (Department d : result) {
            actualTags.add(d.getTag());
        }

        assertArrayEquals("Department (tags) getting failed", expected, actualTags.toArray());
    }

    @Test
    public void testGetDepartmentByNames() throws Exception {
        LDFTestWrapper ldf = new LDFTestWrapper();
        List<Department> result = ldf.getDepartments();

        String[] expected = {"Biology House", "Geography House"};

        List<String> actualNames = new ArrayList<>();
        for (Department d : result) {
            actualNames.add(d.getName());
        }

        assertArrayEquals("Department (names) getting failed", expected, actualNames.toArray());
    }

    @Test
    public void testGetDepartmentsWithMessage() throws Exception {
        LDFTestWrapper ldf = new LDFTestWrapper();
        List<Department> result = ldf.getDepartments();

        String actual = "";
        for (Department d : result) {
            if (d.getTag().equals("bf")) {
                actual = d.getMessage();
            }
        }

        assertEquals("Non-empty message getting failed", "Crucial information", actual);
    }

    @Test
    public void testGetDepartmentsWithNoMessage() throws Exception {
        LDFTestWrapper ldf = new LDFTestWrapper();
        List<Department> result = ldf.getDepartments();

        String actual = "";
        for (Department d : result) {
            if (d.getTag().equals("gf")) {
                actual = d.getMessage();
            }
        }

        assertEquals("Empty message getting failed", "", actual);
    }

//    @Test
//    public void testGetDepartments() throws Exception {
//        LDFTestWrapper ldf = new LDFTestWrapper();
//        List<Department> result = ldf.getDepartments();
//
//        Department[] expected = {new Department("bf", "Biology House", "Crucial Information"),
//                new Department("gf", "Geography House", "")};
//
//        assertArrayEquals("Department getting failed", expected, result.toArray());
//    }


    @Test
    public void testGetGroups() throws Exception {

    }

    @Test
    public void testGetTT() throws Exception {

    }
}

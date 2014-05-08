package org.tt.core.fetch.lexx;

import static org.junit.Assert.*;
import org.junit.Test;

import org.tt.core.entity.datafetcher.Department;

import java.util.List;

/**
 * Created by fau on 07/05/14.
 */
public class LexxDataFetcherTest  {
    @Test
    public void testGetDepartmentsWithMessage() throws Exception {
         LDFTestWrapper ldf = new LDFTestWrapper();
         List<Department> result = ldf.getDepartments();

        String actual = "";
        for (Department d: result)
        {
            if (d.getTag().equals("bf")) {
                actual = d.getMessage();
            }
        }

        assertEquals("Non-empty message getting failed", "Crucial information", actual);
    }

    @Test
    public void testGetGroups() throws Exception {

    }

    @Test
    public void testGetTT() throws Exception {

    }
}

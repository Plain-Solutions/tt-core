package org.tt.core.fetch.lexx;

import static org.junit.Assert.*;
import org.junit.Test;

import org.tt.core.entity.datafetcher.Department;

import java.util.List;

/**
 * Created by fau on 07/05/14.
 */
public class LexxDataFetcherTest  {
    //@Test
    public void testGetDepartmentsWithMessage() throws Exception {
        TestLDF ldf = new TestLDF("mockpass");
        List<Department> result = ldf.getDepartments();
        String expected = "";
        for (Department d: result) {
            if (d.getTag().equals("bf"))
                expected = d.getMessage();
        }
        assertEquals("Non-empty message failure", "Some useful information", expected);
    }

    @Test
    public void testGetGroups() throws Exception {

    }

    @Test
    public void testGetTT() throws Exception {

    }
}

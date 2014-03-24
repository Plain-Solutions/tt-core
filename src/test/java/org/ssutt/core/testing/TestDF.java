package org.ssutt.core.testing;

import org.junit.Assert;
import org.junit.Test;
import org.ssutt.core.fetch.SSUDataFetcher;

import java.util.Map;
import java.util.TreeSet;

/**
 * Created by fau on 24/03/14.
 */
public class TestDF {
    String[] exclusions = {"kgl", "cre", "el"};
    String globalScheduleURL = "http://www.sgu.ru/schedule";

    @Test
    public void testObjectCreation() {
        SSUDataFetcher df = SSUDataFetcher.getInstance(globalScheduleURL, exclusions);

        Assert.assertArrayEquals("DataFetcher creation - failed",exclusions, df.getExclusions());


    }

    @Test
    public void testGetingDataFromSSU() {
        SSUDataFetcher df = SSUDataFetcher.getInstance(globalScheduleURL, exclusions);

        Map<String, String> res = df.getDepartments();
        for (String k : new TreeSet<>(res.keySet()))
            System.out.println(k + " " + res.get(k));
        Assert.assertNotNull("DataFetcher(SSU) getting departments - failed", df.getDepartments());


    }

}

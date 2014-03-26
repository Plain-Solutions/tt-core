/*
Copyright 2014 Plain Solutions

Authors:
   Vlad Slepukhin <slp.vld@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.ssutt.core.testing;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.ssutt.core.fetch.DataFetcher;
import org.ssutt.core.fetch.SSUDataFetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDF {
    private String[] exclusions = {"kgl", "cre", "el"};
    private String globalScheduleURL = "http://www.sgu.ru/schedule";

    private DataFetcher df;

    @Test
    public void aTestObjectCreation() {
        if (df == null)
            df = new SSUDataFetcher(globalScheduleURL, exclusions);
        Assert.assertArrayEquals("DataFetcher(SSU) creation - failed", exclusions, df.getExclusions());
    }

    @Test
    public void bTestGettingDataFromSSU() {
        if (df == null)
            df = new SSUDataFetcher(globalScheduleURL, exclusions);
        Map<String, String> res = df.getDepartments();

        Assert.assertNotNull("DataFetcher(SSU) getting departments - failed", res);
    }

    @Test
    public void cTestGettingGroups() {
        if (df == null)
            df = new SSUDataFetcher(globalScheduleURL, exclusions);

        Map<String, String> res = df.getDepartments();

        List<List<String>> r = new ArrayList<>();
        for (String d : new TreeSet<>(res.keySet()))
            r.add(df.getGroups(d));

        Assert.assertNotNull("DataFetcher(SSU) getting groups - failed", r);
    }
}

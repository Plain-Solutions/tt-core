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

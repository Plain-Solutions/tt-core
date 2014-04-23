package org.tt.core.fetch;

import org.junit.Test;
import org.tt.core.fetch.lexx.LexxDataFetcher;
import org.tt.core.fetch.lexx.entity.Department;
import org.tt.core.fetch.lexx.entity.Lesson;

import java.util.List;

/**
 * Copyright 2014 Plain Solutions
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Author: Avetisyan Sevak
 * Date: 23.04.14.
 */

public class LexxDataFetcherTest {
    //@Test
    public void testDepartments() {
        LexxDataFetcher ldf = new LexxDataFetcher();
        List<Department> departmentList = ldf.getDepartments();

        for (Department d : departmentList) {
            System.out.printf("%s %s %s\n", d.getTag(), d.getName(), d.getMessage());
        }
    }

    //@Test
    public void testGroups() {
        LexxDataFetcher ldf = new LexxDataFetcher();
        List<String> knt = ldf.getGroups("knt");
        System.out.println(knt);
    }

    @Test
    public void testTT() {
        LexxDataFetcher ldf = new LexxDataFetcher();
        List<List<Lesson>> tt = ldf.getTT("knt", "151");

        for (List<Lesson> days : tt) {
            for (Lesson lesson : days) {
                System.out.printf("%d\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%d\n\n",
                        lesson.getSequence(),
                        lesson.getParity(),
                        lesson.getSubgroup().equals("") ? "empty" : lesson.getSubgroup(),
                        lesson.getActivity(),
                        lesson.getSubject(),
                        lesson.getTeacher(),
                        lesson.getBuilding(),
                        lesson.getRoom().equals("") ? "empty" : lesson.getRoom(),
                        lesson.getTimestamp());
            }
        }
    }
}

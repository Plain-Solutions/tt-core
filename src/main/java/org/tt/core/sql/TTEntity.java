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

import java.util.ArrayList;
import java.util.List;

public class TTEntity {
    private List<TTDayEntity> timetable;

    public TTEntity() {
        timetable = new ArrayList<>();
    }

    public void append(String day, String parity, int seq, String activity, String subject, String subgroup,
                       String teacher, String building, String room) {
        for (int i = 0; i < timetable.size(); i++) {
            //if this day already in the list
            if (timetable.get(i).getName().equals(day)) {
                List<TTLesson> lessons = timetable.get(i).getLessons();
                for (TTLesson aLesson: lessons) {
                    if (aLesson.getParity().equals(parity)&&aLesson.getSequence() == seq) {
                        aLesson.append(activity, subject, subgroup, teacher, building, room);
                        return;
                    }
                }
                //else no lessons yet
                TTLesson aLesson = new TTLesson();
                aLesson.setParity(parity);
                aLesson.setSequence(seq);
                aLesson.append(activity, subject, subgroup, teacher, building, room);
                timetable.get(i).append(aLesson);
                return;
            }
        }
        TTDayEntity aDay = new TTDayEntity();
        aDay.setName(day);

        TTLesson aLesson = new TTLesson();
        aLesson.setParity(parity);
        aLesson.setSequence(seq);
        aLesson.append(activity, subject, subgroup, teacher, building, room);
        aDay.append(aLesson);


        timetable.add(aDay);

    }

    public List<TTDayEntity> getTimetable() {
        return timetable;
    }
}

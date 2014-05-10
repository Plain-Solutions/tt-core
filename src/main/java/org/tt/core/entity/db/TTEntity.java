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
package org.tt.core.entity.db;

import java.util.ArrayList;
import java.util.List;

/**
 * The entity which formats DB output to easily serializable format.
 *
 * Refer TT Project <a href=https://github.com/Plain-Solutions/tt-platform/blob/master/docs/API%20Reference.md>API</a>
 *
 * @author Vlad Slepukhin
 * @since 2.0.0
 */
public class TTEntity {
    /**
     * The list of lessons, sorted by days.
     */
    private List<TTDayEntity> timetable;

    /**
     * Constructor. Initialises the list.
     */
    public TTEntity() {
        timetable = new ArrayList<>();
    }

    /**
     * Add a lesson
     * @param day Name of the weekday.
     * @param parity Name of parity state.
     * @param seq The order during the day.
     * @param activity Performed activity.
     * @param subject Name of subject.
     * @param subgroup Name of subgroup.
     * @param teacher Name of teacher.
     * @param building Name of building.
     * @param room Name of room.
     */
    public void append(String day, String parity, int seq, String activity, String subject, String subgroup,
                       String teacher, String building, String room) {
        for (int i = 0; i < timetable.size(); i++) {
            //if this day already in the list
            if (timetable.get(i).getName().equals(day)) {
                List<TTLesson> lessons = timetable.get(i).getLessons();
                for (int j = 0;  j < lessons.size() ; j++) {
                    if (lessons.get(j).getParity().equals(parity)&&lessons.get(j).getSequence() == seq) {
                        lessons.get(j).append(activity, subject, subgroup, teacher, building, room);
                        return;
                    }
                }
                //else no lessons yet

                timetable.get(i).append(createLesson(parity, seq, activity, subject, subgroup, teacher, building, room));
                return;
            }
        }
        TTDayEntity aDay = new TTDayEntity();
        aDay.setName(day);
        aDay.append(createLesson(parity, seq, activity, subject, subgroup, teacher, building, room));


        timetable.add(aDay);

    }

    /**
     * Utility method.
     * @param parity Name of parity state.
     * @param seq The order during the day.
     * @param activity Performed activity.
     * @param subject Name of subject.
     * @param subgroup Name of subgroup.
     * @param teacher Name of teacher.
     * @param building Name of building.
     * @param room Name of room.
     * @return A prepared instance of {@link org.tt.core.entity.db.TTLesson}
     */
    private TTLesson createLesson(String parity, int seq, String activity, String subject, String subgroup,
                                  String teacher, String building, String room)                          {
        TTLesson aLesson = new TTLesson();
        aLesson.setParity(parity);
        aLesson.setSequence(seq);
        aLesson.append(activity, subject, subgroup, teacher, building, room);

        return aLesson;

    }

    public List<TTDayEntity> getTimetable() {
        return timetable;
    }
}

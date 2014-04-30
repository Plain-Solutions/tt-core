/*
 * Copyright 2014 Plain Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tt.core.dm.convert.json.serializer;

import com.google.gson.*;
import org.tt.core.entity.db.TTDayEntity;
import org.tt.core.entity.db.TTEntity;
import org.tt.core.entity.db.TTLesson;

import java.lang.reflect.Type;
import java.util.List;

/**
 * TimeTableSerializer is override of standard GSON <code>JsonSerializer</code> to properly create table of group.
 * It has this structure:
 * <ul>
 * <li>1st: array of days {"dayname": data}</li>
 * <li>2nd: array of lessons (data) {parity, sequence, info} </li>
 * <li>3rd: array of lessons records (info) {activity, subject, subinfo}</li>
 * <li>4th: array of subgroup-teacher-location (subinfo) {subgroup, teacher, building, room} </li>
 * </ul>
 * sequence and info. It is sorted by days, then by sequence of lesson, then by parity (even<odd<full),
 * then by subgroups (lexicographically):
 * <code>
 * { <br>
 * "sat": [ <br>
 * {          <br>
 * "parity": "full", <br>
 * "sequence": 2,        <br>
 * "info": [                 <br>
 * {                           <br>
 * "activity": "practice",       <br>
 * "subject": "Физическая культура", <br>
 * "subinfo": [                          <br>
 * {                                       <br>
 * "subgroup": "",                           <br>
 * "teacher": "Вантеева Виктория Леонидовна",    <br>
 * "building": "12корпус Спортзал",                  <br>
 * "room": ""                                            <br>
 * }                                                           <br>
 * ]                                                             <br>
 * }                                                               <br>
 * ]                                                                 <br>
 * }                                                                   <br>
 * ]                                                                     <br>
 * }                                                                       <br>
 * </code>
 *
 * @author Vlad Slepukhin
 * @since 2.0
 */
public class TimeTableSerializer implements JsonSerializer<TTEntity> {

    /**
     * Converts TimeTableEntity to JsonElement, saving order in information in right representation.
     *
     * @param tt                       an datafetcher of {@link org.tt.core.entity.db.TTEntity}
     * @param type                     default GSON parameter.
     * @param jsonSerializationContext default GSON parameter.
     * @return JsonElement in a proper format.
     * @since 2.0
     */
    @Override
    public JsonElement serialize(TTEntity tt, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray result = new JsonArray();
        List<TTDayEntity> days = tt.getTimetable();
        //for is used instead of foreach for better performance due to high complexity O(n^4)
        for (int i = 0; i < days.size(); i++) {
            JsonObject day = new JsonObject();
            List<TTLesson> lessonEntity = days.get(i).getLessons();
            JsonArray lessons = new JsonArray();
            for (int j = 0; j < lessonEntity.size(); j++) {
                JsonObject entry = new JsonObject();
                entry.addProperty("parity", lessonEntity.get(j).getParity());
                entry.addProperty("sequence", lessonEntity.get(j).getSequence());

                JsonArray subjInfo = new JsonArray();
                List<TTLesson.TTLessonRecord> lrs = lessonEntity.get(j).getRecords();
                for (int k = 0; k < lrs.size(); k++) {
                    JsonObject lrEntry = new JsonObject();
                    lrEntry.addProperty("activity", lrs.get(k).getActivity());
                    lrEntry.addProperty("subject", lrs.get(k).getSubject());


                    JsonArray creInfo = new JsonArray();
                    List<TTLesson.TTClassRoomEntity> cres = lrs.get(k).getClassRoomEntities();


                    for (int m = 0; m < cres.size(); m++) {
                        JsonObject creEntry = new JsonObject();
                        creEntry.addProperty("subgroup", cres.get(m).getSubgroup());
                        creEntry.addProperty("teacher", cres.get(m).getTeacher());
                        creEntry.addProperty("building", cres.get(m).getBuilding());
                        creEntry.addProperty("room", cres.get(m).getRoom());
                        creInfo.add(creEntry);
                    }
                    lrEntry.add("subgroups", creInfo);
                    subjInfo.add(lrEntry);

                }
                entry.add("info", subjInfo);

                lessons.add(entry);
            }
            day.add(days.get(i).getName(), lessons);
            result.add(day);

        }


        return result;
    }
}

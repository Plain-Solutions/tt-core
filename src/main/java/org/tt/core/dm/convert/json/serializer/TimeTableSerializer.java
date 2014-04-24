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
import org.tt.core.sql.TTDayEntity;
import org.tt.core.sql.TTEntity;
import org.tt.core.sql.TTLesson;

import java.lang.reflect.Type;
import java.util.List;

/**
 * TimeTableSerializer is override of standard GSON <code>JsonSerializer</code> to properly create table of group.
 * It is formatted as K-V sorted by day tags with values of array of K-V with all data about lessons: parity,
 * sequence and info. It is sorted by days, then by sequence of lessons and then by parity (even<odd):
 * <code>
 * {"mon":     <br>
 * [      <br>
 * {   "parity":"odd",<br>
 * "sequence":"1",    <br>
 * "info":"lecture. Calculus (II) Sakhno XII, 312"<br>
 * },                                                     <br>
 * {                                                          <br>
 * "parity":"all",                                            <br>
 * "sequence":"2",                                                <br>
 * "info":"practical. Calculus (II) Sakhno XII, 312"                  <br>
 * },                                                                         <br>
 * ...                                                                            <br>
 * ]                                                                                      <br>
 * }                                                                                               <br>
 * </code>
 *
 * @author Vlad Slepukhin
 * @since 1.2
 */
public class TimeTableSerializer implements JsonSerializer<TTEntity> {

    /**
     * Converts TimeTableEntity to JsonElement, saving order in information in right representation.
     *
     * @param tt                       an initialized entity of {@link org.tt.core.dm.convert.json.TimeTableEntity}
     * @param type                     default GSON parameter.
     * @param jsonSerializationContext default GSON parameter.
     * @return JsonElement in a proper format.
     * @since 1.2
     */
    @Override
    public JsonElement serialize(TTEntity tt, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray result = new JsonArray();
        List<TTDayEntity> days = tt.getTimetable();

        for (int i = 0; i < days.size(); i ++)  {
            JsonObject day = new JsonObject();
            List<TTLesson> lessonEntity = days.get(i).getLessons();
            JsonArray lessons = new JsonArray();
            for (int j = 0; j < lessonEntity.size(); j++) {
                JsonObject entry =  new JsonObject();
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
                    lrEntry.add("subinfo", creInfo);
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

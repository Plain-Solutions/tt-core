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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.tt.core.dm.convert.json.entity.TimeTableEntity;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

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
public class TimeTableSerializer implements JsonSerializer<TimeTableEntity> {

    /**
     * Converts TimeTableEntity to JsonElement, saving order in information in right representation.
     *
     * @param tt                       an initialized entity of {@link org.tt.core.dm.convert.json.entity.TimeTableEntity}
     * @param type                     default GSON parameter.
     * @param jsonSerializationContext default GSON parameter.
     * @return JsonElement in a proper format.
     * @since 1.2
     */
    @Override
    public JsonElement serialize(TimeTableEntity tt, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        JsonObject day = new JsonObject();

        List<Map<String, String>> data = tt.getData();

        for (Map<String, String> aLesson : data) {
            JsonObject lesson = new JsonObject();
            for (String key : aLesson.keySet()) {
                lesson.addProperty(key, aLesson.get(key));
            }
            day.add(tt.getWeekday(), lesson);
        }

        result.add(tt.getWeekday(), day);
        return result;
    }
}

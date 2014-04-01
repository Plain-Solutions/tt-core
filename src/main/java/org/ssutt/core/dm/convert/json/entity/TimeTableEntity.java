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

package org.ssutt.core.dm.convert.json.entity;

import java.util.List;
import java.util.Map;

/**
 * TimeTableEntity is a accessor class for {@link org.ssutt.core.dm.convert.json.JSONConverter} to
 * reformat Java Object of {@link org.ssutt.core.dm.SSUDataManager} <code>getTT</code> to JsonObject properly.
 *
 * @author Vlad Slepukhin
 * @since 1.2
 */
public class TimeTableEntity {
    private String weekday;
    private List<Map<String, String>> data;

    /**
     * Creates entity.
     * @param weekday the tag of the day to be iterator in the JsonArray.
     * @param data all the other data about the lesson: parity, sequence and information.
     */
    public TimeTableEntity(String weekday, List<Map<String, String>>  data) {
        this.weekday = weekday;
        this.data = data;
    }

    /**
     * Accessor for {@link org.ssutt.core.dm.convert.json.serializer.TimeTableSerializer}.
     * @return the weekday tag.
     */
    public String getWeekday() {
        return weekday;
    }

    /**
     * Accessor for {@link org.ssutt.core.dm.convert.json.serializer.TimeTableSerializer}.
     * @return the all data about lessons on this day.
     */
    public List<Map<String, String>> getData() {
        return data;
    }
}

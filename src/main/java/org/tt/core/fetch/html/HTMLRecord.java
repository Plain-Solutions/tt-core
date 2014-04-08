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
package org.tt.core.fetch.html;

/**
 * Entity of each cell from the parsed table.
 *
 * @author Vlad Slepukhin
 * @since 1.0
 */
public class HTMLRecord {
    private int weekID;
    private int sequence;
    private int dayID;

    private String info;

    public HTMLRecord() {

    }

    public int getWeekID() {
        return weekID;
    }

    public void setWeekID(int week_id) {
        this.weekID = week_id;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int order) {
        this.sequence = order;
    }

    public int getDayID() {
        return dayID;
    }

    public void setDayID(int day_id) {
        this.dayID = day_id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

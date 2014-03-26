/**
 * Copyright 2014 Plain Solutions
 *
 * Authors:
 * Vlad Slepukhin <slp.vld@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ssutt.core.dm;

public class Record {
    private int week_id;
    private int order;
    private int day_id;

    private String info;

    public Record() {

    }

    public int getWeek_id() {
        return week_id;
    }

    public void setWeek_id(int week_id) {
        this.week_id = week_id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getDay_id() {
        return day_id;
    }

    public void setDay_id(int day_id) {
        this.day_id = day_id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

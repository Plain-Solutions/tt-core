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
package org.ssutt.core.fetch.entities;

import java.util.ArrayList;
import java.util.List;

public class HTMLCellEntity {
    private List<HTMLRecord> cell = new ArrayList<>();

    public HTMLCellEntity() {
    }

    public void addRecord(int weekID, int sequence, int dayID, String info) {
        HTMLRecord r = new HTMLRecord();

        r.setWeekID(weekID);
        r.setSequence(sequence);
        r.setDayID(dayID);
        r.setInfo(info);

        cell.add(r);
    }

    public List<HTMLRecord> getCell() {
        return cell;
    }
}

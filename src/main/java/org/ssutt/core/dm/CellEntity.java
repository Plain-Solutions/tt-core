/**
 * Copyright 2014 Plain Solutions
 * <p/>
 * Authors:
 * Vlad Slepukhin <slp.vld@gmail.com>
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
 */
package org.ssutt.core.dm;

import java.util.ArrayList;
import java.util.List;

public class CellEntity {
    private List<Record> cell = new ArrayList<>();

    public CellEntity() {
    }

    public void addRecord(int weekID, int sequence, int dayID, String info) {
        Record r = new Record();

        r.setWeekID(weekID);
        r.setSequence(sequence);
        r.setDayID(dayID);
        r.setInfo(info);

        cell.add(r);
    }

    public List<Record> getCell() {
        return cell;
    }
}

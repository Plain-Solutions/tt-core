/**
 * Copyright 2014 Plain Solutions
 *
 * Authors:
 * Vlad Slepukhin <slp.vld@gmail.com>
 * Sevak Avetisyan <sevak.avet@gmail.com>
 *
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

import org.ssutt.core.dm.entities.CellEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class TableParser {

    private static final String ev = "чис.";
    private static final String od = "знам.";

    public static CellEntity parseCell(String cell, int row, int column) {
        List<String> parsed = splitCell(cell);

        //do we need two records?
        if (parsed.contains("even")) {
            return createEntity(1, row + 1, column + 1, parsed.get(0));
        }
        if (parsed.contains("odd")) {
            return createEntity(2, row + 1, column + 1, parsed.get(0));
        }
        if (parsed.contains("eq")) {
            return createEntity(3, row + 1, column + 1, parsed.get(0));
        }

        CellEntity ce = new CellEntity();

        /*
            params of addRecord;
            int week_id
            int order
            int day_id
            String info
        */

        ce.addRecord(1, row + 1, column + 1, parsed.get(0));

        ce.addRecord(2, row + 1, column + 1, parsed.get(1));

        return ce;
    }

    private static List<String> splitCell(String cell) {
        List<String> result = new ArrayList<>();

        if (((cell.indexOf(ev)) != -1) && (cell.contains(od))) {
            if (cell.indexOf(od) > cell.indexOf(ev)) {
                result.add(cell.substring(0, cell.indexOf(od)).replace(ev, ""));
                result.add(cell.substring(cell.indexOf(od), cell.length() - 1).replace(od, ""));
                return result;
            } else {
                result.add(cell.substring(0, cell.indexOf(ev)).replace(od, ""));
                result.add(cell.substring(cell.indexOf(ev), cell.length() - 1).replace(ev, ""));
                return result;
            }
        }

        if ((cell.contains(ev))) {
            //has even marker, in the beggining of the entities and has no odd marker
            if ((cell.indexOf(ev) == 0) && (cell.indexOf(od)) == -1) {
                result.add(cell.replace(ev, ""));
                result.add("even");
                return result;
            }
            //FAGGOT MODE: has only even marker, although classes are even/odd  (found @ bf/211)
            if ((cell.indexOf(ev) != 0) && (cell.indexOf(od)) == -1) {
                result.add(cell.substring(cell.indexOf(ev), cell.length() - 1).replace(ev, ""));
                result.add(cell.substring(0, cell.indexOf(ev)));
                return result;
            }
        }

        //same for odd
        if ((cell.contains(od))) {
            //has even marker, in the beggining of the entities and has no odd marker
            if ((cell.indexOf(od) == 0) && (!cell.contains(ev))) {
                result.add(cell.replace(od, ""));
                result.add("odd");
                return result;
            }
            //FAGGOT MODE: has only odd marker, although classes are even/odd  (found @ bf/211 as well)
            if ((cell.indexOf(od) != 0) && (cell.contains(ev))) {
                result.add(cell.substring(cell.indexOf(od), cell.length() - 1).replace(od, ""));
                result.add(cell.substring(0, cell.indexOf(od)));
                return result;
            }
        }

        //has no markers, both weeks
        result.add(cell);
        result.add("eq");
        return result;
    }

    private static CellEntity createEntity(int weekID, int sequence, int dayID, String info) {
        CellEntity ce = new CellEntity();
        ce.addRecord(weekID, sequence, dayID, info);
        return ce;
    }
}

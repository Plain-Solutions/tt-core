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

import java.util.ArrayList;
import java.util.List;

public abstract class TableParser {

    private static final String ev = "чис.";
    private static final String od = "знам.";

    public static CellEntity parseCell(String cell, int row, int column) {
        List<String> parsed = splitCell(cell);

        CellEntity ce = new CellEntity();

        //do we need two records?
        if (parsed.contains("even")) {
            int week_id = 1;
            int order = row+1;
            int day_id = column+1;
            String info = parsed.get(0);

            ce.addRecord(week_id, order, day_id, info);

            return ce;
        }
        if (parsed.contains("odd"))  {
            int week_id = 2;
            int order = row+1;
            int day_id = column+1;
            String info = parsed.get(0);

            ce.addRecord(week_id, order, day_id, info);
            return ce;
        }
        if (parsed.contains("eq")) {
            int week_id = 3;
            int order = row+1;
            int day_id = column+1;
            String info = parsed.get(0);

            ce.addRecord(week_id, order, day_id, info);
            return ce;
        }

        int week_id = 1;
        int order = row+1;
        int day_id = column+1;
        String info = parsed.get(0);

        ce.addRecord(week_id, order, day_id, info);

        week_id = 2;
        info = parsed.get(1);

        ce.addRecord(week_id, order, day_id, info);

        return ce;
    }

    private static List<String> splitCell(String cell) {
        List<String> result = new ArrayList<>();

        if (((cell.indexOf(ev)) != -1) && (cell.indexOf(od) != -1)) {
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

        if ((cell.indexOf(ev) != -1)) {
            //has even marker, in the beggining of the cell and has no odd marker
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
        if ((cell.indexOf(od) != -1)) {
            //has even marker, in the beggining of the cell and has no odd marker
            if ((cell.indexOf(od) == 0) && (cell.indexOf(ev)) == -1) {
                result.add(cell.replace(od, ""));
                result.add("odd");
                return result;
            }
            //FAGGOT MODE: has only odd marker, although classes are even/odd  (found @ bf/211 as well)
            if ((cell.indexOf(od) != 0) && (cell.indexOf(ev)) == -1) {
                result.add(cell.substring(cell.indexOf(od), cell.length() - 1).replace(od, ""));
                result.add(cell.substring(0, cell.indexOf(od)));
                return result;
            }
        }

        //has no markers, both weeks
        result.add(cell);
        result.add("both");
        return result;
    }


}

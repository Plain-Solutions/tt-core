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
package org.ssutt.core.dm.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableEntityFactory {
    List<String[]> table = Collections.emptyList();

    public TableEntityFactory() {
    }

    public void supplyOriginalTable(List<String[]> table) {
        this.table = table;
    }

    public TableEntity produceTableEntity() {
        TableEntity te = new SSUTableEntity();

        int max = getMaxClass();

        te.initializeTables(max);

        for (String[] lesson : table) {
            int parity = getParity(lesson[0]);
            int dayID = getDayID(lesson[1]);
            putLesson(te, dayID, Integer.parseInt(lesson[2]), parity, lesson[3]);
        }

        return te;
    }


    private int getMaxClass() {
        List<Integer> sequences = new ArrayList<>();
        for (String[] lesson : table) {
            sequences.add(Integer.parseInt(lesson[2]));
        }
        return Collections.max(sequences);
    }

    private int getParity(String parity) {
        int id = 0;
        switch (parity) {

            case "even":
                id = 0;
                break;
            case "odd":
                id = 1;
                break;
            case "both":
                id = 2;
                break;

        }
        return id;
    }

    private int getDayID(String day) {
        int id = 1;
        switch (day) {
            case "mon":
                break;
            case "tue":
                id = 2;
                break;
            case "wed":
                id = 3;
                break;
            case "thu":
                id = 4;
                break;
            case "fri":
                id = 5;
                break;
            case "sat":
                id = 6;
                break;
        }
        return id;
    }

    private void putLesson(TableEntity te, int day, int sequence, int parity, String info) {
        if (parity == 0)
            te.putEvenLesson(info, day, sequence);
        if (parity == 1)
            te.putOddLesson(info, day, sequence);
        if (parity == 2)
            te.putEvenLesson(info, day, sequence);
        te.putOddLesson(info, day, sequence);
    }
}



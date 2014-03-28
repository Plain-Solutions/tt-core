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

public class SSUTableEntity implements TableEntity {
    String[][] evenTable;
    String[][] oddTable;


    @Override
    public void initializeTables(int maxClasses) {
        evenTable = new String[6][maxClasses];
        oddTable = new String[6][maxClasses];

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < maxClasses; j++)
                evenTable[i][j] = oddTable[i][j] = "";
        }
    }

    @Override
    public void putEvenLesson(String info, int day, int sequence) {
        evenTable[day - 1][sequence - 1] = info;
    }

    @Override
    public void putOddLesson(String info, int day, int sequence) {
        oddTable[day - 1][sequence - 1] = info;
    }

    public String[][] getEvenTable() {
        return evenTable;
    }

    public String[][] getOddTable() {
        return oddTable;
    }
}

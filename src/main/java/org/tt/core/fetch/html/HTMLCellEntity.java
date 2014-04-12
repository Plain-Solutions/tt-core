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

import java.util.ArrayList;
import java.util.List;

/**
 * HTMLCelEntity is a class to convert HTML-formatted table output to database-friendly format. It just stores
 * data in a format, which can be accessed with ease. Actually, it is a factory with warehouse.
 *
 * Entity has
 * <li>
 *     <ul><code>weekID</code> - ID for the database of parity</ul>
 *     <ul><code>sequence</code> - order of lesson during the day</ul>
 *     <ul><code>dayID</code> - ID of the day (mon - 1)</ul>
 *     <ul><code>info</code> the whole info about parsed lesson</ul>
 * </li>
 *
 * @author Vlad Slepukhin
 * @since 1.0
 */
public class HTMLCellEntity {
    private List<HTMLRecord> cell = new ArrayList<>();

    public HTMLCellEntity() {
    }

    /**
     * Adds record to the list of lessons/
     * @param weekID  ID for the database of parity.
     * @param sequence order through day.
     * @param dayID day ID (mon -1)
     * @param info all the data
     */
    public void addRecord(int weekID, int sequence, int dayID, String info) {
        HTMLRecord r = new HTMLRecord();

        r.setWeekID(weekID);
        r.setSequence(sequence);
        r.setDayID(dayID);
        r.setInfo(info);

        cell.add(r);
    }

    /**
     * Accessor for usage in the DB.
     * @return List of {@link org.tt.core.fetch.html.HTMLRecord} entities
     */
    public List<HTMLRecord> getCell() {
        return cell;
    }
}

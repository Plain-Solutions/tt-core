/**
 * Copyright 2014 Plain Solutions
 *
 * Authors:
 *  Vlad Slepukhin <slp.vld@gmail.com>
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

import org.ssutt.core.dm.entities.HTMLCellEntity;
import org.ssutt.core.dm.entities.HTMLRecord;
import org.ssutt.core.dm.entities.TableEntity;
import org.ssutt.core.dm.entities.TableEntityFactory;
import org.ssutt.core.fetch.DataFetcher;
import org.ssutt.core.fetch.SSUDataFetcher;
import org.ssutt.core.sql.SQLManager;
import org.ssutt.core.sql.SSUSQLManager;
import org.ssutt.core.sql.ex.EmptyTableException;
import org.ssutt.core.sql.ex.NoSuchDepartmentException;
import org.ssutt.core.sql.ex.NoSuchGroupException;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SSUDataManager implements DataManager {
    private String[] exclusions = {"kgl", "cre", "el"};
    private String globalScheduleURL = "http://www.sgu.ru/schedule";

    private DataFetcher df;
    private SQLManager sqlm;

    public SSUDataManager() {
    }

    @Override
    public void deliverDBProvider(Connection conn) {
        sqlm = new SSUSQLManager(conn);
    }

    @Override
    public void deliverDataFetcherProvider() {
        df = new SSUDataFetcher(globalScheduleURL, exclusions);
    }

    @Override
    public void putDepartments() throws SQLException {
        sqlm.putDepartments(df.getDepartments());
    }

    @Override
    public void putDepartmentGroups(String departmentTag) throws SQLException, NoSuchDepartmentException {
         sqlm.putGroups(df.getGroups(departmentTag), departmentTag);
    }

    @Override
    public Map<String, String> getDepartments() throws SQLException {
        return sqlm.getDepartments();
    }

    @Override
    public List<String> getDepartmentTags() throws SQLException {
        return sqlm.getDepartmentTags();
    }

    @Override
    public void putAllGroups() throws SQLException, NoSuchDepartmentException {
        for (String departmentTag : getDepartmentTags())
            putDepartmentGroups(departmentTag);
    }

    @Override
    public List<String> getGroups(String departmentTag) throws SQLException, NoSuchDepartmentException {
        return sqlm.getGroups(departmentTag);
    }

    @Override
    public int getGroupID(String departmentTag, String groupName) throws SQLException, NoSuchDepartmentException,
            NoSuchGroupException {
        return sqlm.getGroupID(departmentTag, groupName);
    }

    @Override
    public TableEntity getTT(int groupID) throws EmptyTableException, SQLException, NoSuchGroupException {
        List<String[]> table = sqlm.getTT(groupID);

        TableEntityFactory tf = new TableEntityFactory();
        tf.supplyOriginalTable(table);

        return tf.produceTableEntity();

    }

    @Override
    public void putTT(String departmentTag, int groupID) throws IOException, SQLException,
            NoSuchDepartmentException, NoSuchGroupException {
        //first, we get timetable url
        String groupName = sqlm.getGroupName(departmentTag, groupID);

        if (sqlm.groupExistsInDepartment(departmentTag, groupName)) {
            String groupAddress =
                    (df.getNonNumericalGroups().containsKey(groupName)) ?
                            df.getNonNumericalGroups().get(groupName) : groupName;

            String url = String.format("%s/%s/%s/%s", globalScheduleURL, departmentTag, "do", groupAddress);
            System.out.println(url);
            //and its contents
            String[][] table = df.getTT(new URL(url));
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 6; j++) {
                    //we swap row and column as in any timetable rows are classes and columns are days
                    //but in the df.getTT it's vice verse
                    HTMLCellEntity ce = TableParser.parseCell(table[i][j], i, j);
                    for (HTMLRecord r : ce.getCell()) {
                           /*
                           We need to clarify one thing
                           DataManager works only RAW data, that's why we pass RAW data to
                           SQLManager, though we can pass Record, but
                           the level of abstraction on DB site should be as high as it can be
                           That's why you won't see JSON Parser in DataManger, it's a part of
                           tt-platform and its task: convert raw data for end-user
                           */

                        //skip empty classes
                        if (r.getInfo().length() != 0) {
                            int grpID = sqlm.getGroupID(departmentTag, groupName);
                            int dtID = sqlm.putDateTime(r.getWeekID(), r.getSequence(), r.getDayID());
                            int subjID = sqlm.putSubject(r.getInfo());
                            sqlm.putLessonRecord(grpID, dtID, subjID);
                        }
                    }
                }
            }
            System.out.println(url + " passed");
        }
    }

}

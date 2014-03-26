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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ssutt.core.fetch.DataFetcher;
import org.ssutt.core.fetch.SSUDataFetcher;
import org.ssutt.core.sql.SQLManager;
import org.ssutt.core.sql.SSUSQLManager;
import org.ssutt.core.sql.ex.NoSuchDepartmentException;
import org.ssutt.core.sql.ex.NoSuchGroupException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SSUDataManager implements DataManager {
    private static final Logger logger = LogManager.getLogger(SSUDataManager.class.getName());

    private String[] exclusions = {"kgl", "cre", "el"};
    private String globalScheduleURL = "http://www.sgu.ru/schedule";

    private DataFetcher df;
    private SQLManager sqlm;

    public SSUDataManager() {
        logger.info("DataManager (SSU) was created");
    }

    @Override
    public void deliverDBProvider(Connection conn) {
        sqlm = new SSUSQLManager(conn);
        logger.info("DataManager: DBManager: SSUSQLManager");
    }

    @Override
    public void deliverDataFetcherProvider() {
        df = new SSUDataFetcher(globalScheduleURL, exclusions);
        logger.info("DataManager: DataFetcher: SSUDataFetcher");
    }

    @Override
    public void putDepartments() {
        try {
            sqlm.putDepartments(df.getDepartments());
        } catch (SQLException e) {
            logger.fatal(e);
        }
    }

    @Override
    public Map<String, String> getDepartments() {
        Map<String, String> result;
        try {
            result = sqlm.getDepartments();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
        return result;
    }

    @Override
    public List<String> getDepartmentTags() {
        List<String> result;
        try {
            result = sqlm.getDepartmentTags();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
        return result;
    }

    @Override
    public void putGroups() {
        try {
            for (String department : getDepartmentTags())
                sqlm.putGroups(df.getGroups(department), department);
        } catch (SQLException | NoSuchDepartmentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getGroups(String departmentTag) {
        List<String> result;
        try {
            result = sqlm.getGroups(departmentTag);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        } catch (NoSuchDepartmentException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
        return result;
    }

    @Override
    public void putTT(String departmentTag, String groupName) {
        try {
            //first, we get timetable url
            if (sqlm.groupExists(departmentTag, groupName)) {
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
                        CellEntity ce = TableParser.parseCell(table[i][j], i, j);
                        for (Record r : ce.getCell()) {
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchGroupException e) {
            e.printStackTrace();
        } catch (NoSuchDepartmentException e) {
            e.printStackTrace();
        }
    }

}

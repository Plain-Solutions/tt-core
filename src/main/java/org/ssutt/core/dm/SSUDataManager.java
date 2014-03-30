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
package org.ssutt.core.dm;

import org.ssutt.core.fetch.TTDataFetcher;
import org.ssutt.core.fetch.entities.HTMLCellEntity;
import org.ssutt.core.fetch.entities.HTMLRecord;
import org.ssutt.core.fetch.entities.TableParser;
import org.ssutt.core.sql.Queries;
import org.ssutt.core.sql.TTSQLManager;
import org.ssutt.core.sql.ex.EmptyTableException;
import org.ssutt.core.sql.ex.NoSuchDepartmentException;
import org.ssutt.core.sql.ex.NoSuchGroupException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * SSUDataManager is a implementation of TTDataManager which works with SSU schedule page.
 * It connects:
 * <ul>
 * <li>SSUSQLManager - database connection interface</li>
 * <li>SSUDataFetcher - utilities to parse HTML on SSU schedules pages</li>
 * </ul>
 * However, the main aim of TTDataManager is to manage data, especially raw data - text strings and collections of them.
 * We are trying to keep this idea working and handle string and sometimes some structures, where it seems easy,
 * correct and what it important for TT Platform - JSON-friendly.
 *
 * @author Vlad Slepukhin, Nickolay Yurin
 * @since 1.0
 */
public class SSUDataManager implements TTDataManager {

    /**
     * Class instance of TTDataFetcher to be accessible around the DM independently.
     */
    private TTDataFetcher df;
    /**
     * Class instance of SQLManager to be accessible around the DM independently.
     */
    private TTSQLManager sqlm;

    /**
     * Constructor now is empty.
     */
    public SSUDataManager() {
    }

    /**
     * Connect TTDataFetcher to get departments map and store it into DB with TTSQLManager.
     *
     * @throws SQLException
     */
    @Override
    public void putDepartments() throws SQLException {
        sqlm.putDepartments(df.getDepartments());
    }

    /**
     * Connect TTDataFetcher to get list of groups on <b>department</b> and store it into DB with TTSQLManager.
     *
     * @param departmentTag the tag (token) of the department.
     * @throws SQLException
     * @throws NoSuchDepartmentException
     */
    @Override
    public void putDepartmentGroups(String departmentTag) throws SQLException, NoSuchDepartmentException {
        sqlm.putGroups(df.getGroups(departmentTag), departmentTag);
    }

    /**
     * Connect TTDataFetcher to get list of <b>ALL</b> groups and store it into DB.
     *
     * @throws SQLException
     * @throws NoSuchDepartmentException
     */
    @Override
    public void putAllGroups() throws SQLException, NoSuchDepartmentException {
        for (String departmentTag : getDepartmentTags())
            putDepartmentGroups(departmentTag);
    }

    /**
     * Get the map of stored departments.
     *
     * @return Map of departments represented by name-tag.
     * @throws SQLException
     */
    @Override
    public List<String[]> getDepartments() throws SQLException {
        return sqlm.getDepartments();
    }

    /**
     * Get list of the tags for each department. TT Platform&Servlets are tied around them.
     *
     * @return list of tags.
     * @throws SQLException
     */
    @Override
    public List<String> getDepartmentTags() throws SQLException {
        return sqlm.getDepartmentTags();
    }

    /**
     * Get displayable group names (151, 451) as Strings (see non-numerical groups) on specified department.
     *
     * @param departmentTag the tag of the department.
     * @return list of Strings(!) - names.
     * @throws SQLException
     * @throws NoSuchDepartmentException
     */
    @Override
    public List<String> getGroups(String departmentTag) throws SQLException, NoSuchDepartmentException {
        return sqlm.getGroups(departmentTag);
    }

    /**
     * Converts name of group of the specified department (allocation check, throws NoSuchGroupException) to its global
     * id in the database. Used by TT Platform&Servlets.
     *
     * @param departmentTag the tag of the department.
     * @param groupName     the displayable name.
     * @return global ID.
     * @throws SQLException
     * @throws NoSuchDepartmentException
     * @throws NoSuchGroupException
     */
    @Override
    public int getGroupID(String departmentTag, String groupName) throws SQLException, NoSuchDepartmentException,
            NoSuchGroupException {
        return sqlm.getGroupID(departmentTag, groupName);
    }

    /**
     * Get nearly raw, but formatted timetable output from the database.
     *
     * @param groupID the global id of group to get the timetable.
     * @return <\ListString[]\> - the raw result of what stored in the database. Each list element is a lesson,
     * String[4] contains parity, day, sequence during this day and the information. Since it we don't need
     * TableEntity and DataManager operates really with raw data.
     * @throws EmptyTableException
     * @throws SQLException
     * @throws NoSuchGroupException
     */
    @Override
    public List<String[]> getTT(int groupID) throws EmptyTableException, SQLException, NoSuchGroupException {
        return sqlm.getTT(groupID);
    }

    /**
     * Adding temporary table from TTDataFetcher (parsed by HTML tags table) in a proper format to DB.
     * <p/>
     * We need to clarify one thing
     * TTDataManager works only RAW data, that's why we pass RAW data to
     * TTSQLManager, though we can pass Record, but
     * the level of abstraction on DB site should be as high as it can be
     * That's why you won't see JSON Parser in TTDataManger, it's a part of
     * tt-platform and its task: convert raw data for end-user
     *
     * @param departmentTag the tag of the department, where the group is located (allocation check).
     * @param groupID       the global id of group.
     * @throws IOException
     * @throws SQLException
     * @throws NoSuchDepartmentException
     * @throws NoSuchGroupException
     */
    @Override
    public void putTT(String departmentTag, int groupID) throws IOException, SQLException,
            NoSuchDepartmentException, NoSuchGroupException {
        //first, we get timetable url
        String groupName = sqlm.getGroupName(departmentTag, groupID);

        //then we check that such group exists in the specified department
        if (sqlm.groupExistsInDepartment(departmentTag, groupName)) {
            //and its contents
            String[][] table = df.getTT(df.formatURL(departmentTag, groupName));
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 6; j++) {
                    HTMLCellEntity ce = TableParser.parseCell(table[i][j], i, j);
                    for (HTMLRecord r : ce.getCell()) {
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
        }
    }

    /**
     * Get the provider, TTSQLManager requires an instance of TTSQLManager implementation (for instance, SSUSQLManager
     * and Queries implementation for specified database (H2Queries, for example).
     * initialized before. For instance, in SSU TT project we do it with init script (here, in resources for testing)
     * and JNDI in Tomcat.
     *
     * @param sqlm TTSQLManager instance realization.
     * @param qrs Queries instance realization to acquire queries definitions.
     */
    @Override
    public void deliverDBProvider(TTSQLManager sqlm, Queries qrs) {
        this.sqlm = sqlm;
        sqlm.setQueries(qrs);
    }

    /**
     * Get the provider of data fetching utilities. We made this abstraction to be able to create a fork for other
     * universities.
     *
     * @param df a created instance of TTDataFetcher implementation (for instance, SSUDataFetcher).
     */
    @Override
    public void deliverDataFetcherProvider(TTDataFetcher df) {
        this.df = df;
    }
}

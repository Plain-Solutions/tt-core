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
package org.tt.core.dm;

import org.tt.core.dm.convert.json.JSONConverter;
import org.tt.core.fetch.AbstractDataFetcher;
import org.tt.core.fetch.ssudf.html.HTMLCellEntity;
import org.tt.core.fetch.ssudf.html.HTMLRecord;
import org.tt.core.fetch.ssudf.html.TableParser;
import org.tt.core.sql.AbstractQueries;
import org.tt.core.sql.AbstractSQLManager;
import org.tt.core.sql.ex.EmptyTableException;
import org.tt.core.sql.ex.NoSuchDepartmentException;
import org.tt.core.sql.ex.NoSuchGroupException;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * SSUDataManager is a implementation of AbstractDataManager which works with SSU schedule page.
 * It connects:
 * <ul>
 * <li>SSUSQLManager - database connection interface</li>
 * <li>SSUDataFetcher - utilities to parse HTML on SSU schedules pages</li>
 * </ul>
 * However, the main aim of AbstractDataManager is to manage data, especially raw data - text strings and collections of them.
 * We are trying to keep this idea working and handle string and sometimes some structures, where it seems easy,
 * correct and what it important for TT Platform - JSON-friendly.
 *
 * @author Vlad Slepukhin, Nickolay Yurin
 * @since 1.0
 */
public class SSUDataManager implements AbstractDataManager {

    private AbstractDataFetcher df;
    private AbstractSQLManager sqlm;

    private AbstractDataConverter dconv;

    private String scheduleURL = "";

    /**
     * Constructor with no params now is empty.
     */
    public SSUDataManager() {
    }

    /**
     * Constructor with all provided entity of data suppliers.
     *
     * @param sqlm            SQLManager instance.
     * @param qrs             Queries for SQL instance.
     * @param df              DataFetcher instance.
     * @param dconv           DataConverter instance.
     * @param globalURLString Global schedule URL to use in data fetching
     * @see org.tt.core.sql.AbstractSQLManager
     * @see org.tt.core.sql.AbstractQueries
     * @see org.tt.core.fetch.AbstractDataFetcher
     * @see org.tt.core.dm.AbstractDataConverter
     * @since 1.3
     */
    public SSUDataManager(AbstractSQLManager sqlm,
                          AbstractQueries qrs,
                          AbstractDataFetcher df,
                          AbstractDataConverter dconv,
                          String globalURLString) {
        deliverDBProvider(sqlm, qrs);
        deliverDataFetcherProvider(df);
        deliverDataConverterProvider(dconv);
        deliverGlobalURL(globalURLString);
    }

    /**
     * Connect AbstractDataFetcher to get departments map and store it into DB with AbstractSQLManager.
     *
     * @return TTData
     * @see org.tt.core.dm.AbstractDataConverter
     */
    @Override
    public TTData putDepartments() {
        TTData result = new TTData();
        try {
            sqlm.putDepartments(df.getDepartments(df.fetch(scheduleURL, false)));
            result.setHttpCode(200);
            result.setMessage(dconv.convertStatus(TTStatus.OK, TTStatus.OKMSG));
        } catch (SQLException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.GENSQL, e.getSQLState()));
        } catch (IOException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.IO, TTStatus.IOERR));
        }
        return result;
    }

    /**
     * Connect AbstractDataFetcher to get list of groups on <b>department</b> and store it into DB with AbstractSQLManager.
     *
     * @param departmentTag the tag (token) of the department.
     * @return TTData
     * @see org.tt.core.dm.AbstractDataConverter
     * @since 1.0
     */
    @Override
    public TTData putDepartmentGroups(String departmentTag) {
        TTData result = new TTData();
        try {
            String url = scheduleURL.concat("/" + departmentTag);
            sqlm.putGroups(df.getGroups(df.fetch(url, false), departmentTag), departmentTag);
            result.setHttpCode(200);
            result.setMessage(dconv.convertStatus(TTStatus.OK, TTStatus.OKMSG));
        } catch (SQLException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.GENSQL, e.getSQLState()));
        } catch (NoSuchDepartmentException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.TTSQL, TTStatus.DEPARTMENTERR));
        } catch (IOException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.IO, TTStatus.IOERR));
        }
        return result;
    }

    /**
     * Connect AbstractDataFetcher to get list of <b>ALL</b> groups and store it into DB.
     *
     * @return TTData
     * @see org.tt.core.dm.AbstractDataConverter
     * @since 1.0
     */
    @Override
    public TTData putAllGroups() {
        TTData result = new TTData();
        try {
            for (String departmentTag : sqlm.getDepartmentTags())
                putDepartmentGroups(departmentTag);
            result.setHttpCode(200);
            result.setMessage(dconv.convertStatus(TTStatus.OK, TTStatus.OKMSG));
        } catch (SQLException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.GENSQL, e.getSQLState()));
        }
        return result;
    }

    /**
     * Adding temporary table from AbstractDataFetcher (parsed by HTML tags table) in a proper format to DB.
     * <p/>
     * We need to clarify one thing
     * AbstractDataManager works only RAW data, that's why we pass RAW data to
     * AbstractSQLManager, though we can pass Record, but
     * the level of abstraction on DB site should be as high as it can be.
     *
     * @param departmentTag the tag of the department, where the group is located (allocation check).
     * @param groupID       the global id of group.
     * @since 1.0
     */
    @Override
    public TTData putTT(String departmentTag, int groupID) {
        TTData result = new TTData();
        try {
            //first, we get timetable url
            String groupName = sqlm.getGroupName(departmentTag, groupID);

            //then we check that such group exists in the specified department
            if (sqlm.groupExistsInDepartment(departmentTag, groupName)) {
                //and its contents
                df.setGlobalURL(scheduleURL);
                URL url = df.formatURL(departmentTag, groupName);
                String[][] table = df.getTT(df.fetch(url.toString(), false));
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
            result.setHttpCode(200);
            result.setMessage(dconv.convertStatus(TTStatus.OK, TTStatus.OKMSG));
        } catch (SQLException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.GENSQL, e.getSQLState()));
        } catch (NoSuchDepartmentException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.TTSQL, TTStatus.DEPARTMENTERR));
        } catch (NoSuchGroupException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.TTSQL, TTStatus.GROUPERR));
        } catch (IOException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.IO, TTStatus.IOERR));
        }
        return result;
    }

    /**
     * Adding temporary table from AbstractDataFetcher (parsed by HTML tags table) in a proper format to DB.
     *
     * @param departmentTag the tag of the department, where the group is located (allocation check).
     * @param groupName     the name of the group
     * @return TTData with <code>httpCode</code> 200
     * <code>module:ok</code> and empty message String in case of success or error trace.
     * @since 1.3
     */
    @Override
    public TTData putTT(String departmentTag, String groupName) {
        TTData result = new TTData();
        try {
            int id = sqlm.getGroupID(departmentTag, groupName);
            result = putTT(departmentTag, id);
        } catch (SQLException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.GENSQL, e.getSQLState()));
        } catch (NoSuchGroupException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.TTSQL, TTStatus.GROUPERR));
        } catch (NoSuchDepartmentException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.TTSQL, TTStatus.DEPARTMENTERR));
        }
        return result;
    }


    /**
     * Get the map of stored departments.
     *
     * @return TTData instance with JSON-formatted String and success/error code.
     * @see org.tt.core.dm.convert.json.serializer.DepartmentSerializer
     * @since 1.1
     */
    @Override
    public TTData getDepartments() {
        TTData result = new TTData();
        try {
            Map<String, Map<String, String>> raw = sqlm.getDepartments();
            result.setHttpCode(200);
            result.setMessage(dconv.convertDepartmentList(raw));
        } catch (SQLException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.GENSQL, e.getSQLState()));
        }
        return result;
    }

    /**
     * Get list of the tags for each department. TT Platform&Servlets are tied around them.
     *
     * @return JSON List of department tags.
     * @since 1.0
     */
    @Override
    public TTData getDepartmentTags() {
        TTData result = new TTData();
        try {
            List<String> raw = sqlm.getDepartmentTags();
            result.setHttpCode(200);
            result.setMessage(dconv.convertAbstractList(raw));
        } catch (SQLException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.GENSQL, e.getSQLState()));
        }
        return result;
    }

    /**
     * Get displayable group names (151, 451) as Strings (see non-numerical groups in
     * {@link org.tt.core.fetch.ssudf.SSUDataFetcher})on specified department.
     *
     * @param departmentTag the tag of the department.
     * @return JSON List of tags.
     * @since 1.0
     */
    @Override
    public TTData getGroups(String departmentTag) {
        TTData result = new TTData();
        try {
            List<String> raw = sqlm.getGroups(departmentTag);
            result.setHttpCode(200);
            result.setMessage(dconv.convertGroupList(raw));
        } catch (SQLException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.GENSQL, e.getSQLState()));
        } catch (NoSuchDepartmentException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.TTSQL, TTStatus.DEPARTMENTERR));
        }
        return result;
    }

    /**
     * Converts name of group of the specified department (allocation check, throws NoSuchGroupException) to its global
     * id in the database. Used by TT Platform&Servlets.
     *
     * @param departmentTag the tag of the department.
     * @param groupName     the displayable name.
     * @return TTData instance with JSON-formatted String and success/error code.
     * @since 1.0
     */
    @Override
    public TTData getGroupID(String departmentTag, String groupName) {
        TTData result = new TTData();
        try {
            int raw = sqlm.getGroupID(departmentTag, groupName);
            result.setHttpCode(200);
            result.setMessage(dconv.convertGroupName(raw));
        } catch (SQLException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.GENSQL, e.getSQLState()));
        } catch (NoSuchDepartmentException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.TTSQL, TTStatus.DEPARTMENTERR));
        } catch (NoSuchGroupException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.TTSQL, TTStatus.GROUPERR));
        }
        return result;
    }

    /**
     * Get nearly raw, but formatted timetable output from the database and process it to some web-friendly format.
     *
     * @param groupID the global id of group to get the timetable.
     * @return TTData instance with JSON-formatted String and success/error code.
     * @see org.tt.core.dm.convert.json.serializer.TimeTableSerializer
     * @since 1.1
     */
    @Override
    public TTData getTT(int groupID) {
        TTData result = new TTData();
        try {
            List<String[]> raw = sqlm.getTT(groupID);
            result.setHttpCode(200);
            result.setMessage(dconv.convertTT(raw));
        } catch (SQLException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.GENSQL, e.getSQLState()));
        } catch (NoSuchGroupException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.TTSQL, TTStatus.GROUPERR));
        } catch (EmptyTableException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.TTSQL, TTStatus.TABLERR));
        }
        return result;
    }

    /**
     * Get some kind of formatted K-V structure
     * @param list
     * @return formatted output
     * @see org.tt.core.dm.convert.json.JSONConverter
     * @since 1.2
     */
    @Override
    public TTData getFormattedString(Map<String, String> list) {
        TTData result = new TTData();
        result.setHttpCode(200);
        result.setMessage(dconv.convertMap(list));
        return result;
    }

    /**
     * Get nearly raw, but formatted timetable output from the database and process it to some web-friendly format.
     *
     * @param departmentTag the tag of the department
     * @param groupName     the displayable name.
     * @return TTData instance with JSON-formatted String and success/error code.
     * @see org.tt.core.dm.convert.json.serializer.TimeTableSerializer
     * @since 1.3
     */
    @Override
    public TTData getTT(String departmentTag, String groupName) {
        TTData result = new TTData();
        try {
            int groupID = sqlm.getGroupID(departmentTag, groupName);
            result = getTT(groupID);
        } catch (SQLException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.GENSQL, e.getSQLState()));
        } catch (NoSuchDepartmentException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.TTSQL, TTStatus.DEPARTMENTERR));
        } catch (NoSuchGroupException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.TTSQL, TTStatus.GROUPERR));
        }

        return result;
    }


    /**
     * Get the provider, AbstractSQLManager requires an instance of AbstractSQLManager implementation (for instance, SSUSQLManager
     * and AbstractQueries implementation for specified database (H2Queries, for example).
     * initialized before. For instance, in SSU TT project we do it with init script (here, in resources for testing)
     * and JNDI in Tomcat.
     *
     * @param sqlm AbstractSQLManager instance realization.
     * @param qrs  AbstractQueries instance realization to acquire queries definitions.
     * @since 1.1
     */
    @Override
    public void deliverDBProvider(AbstractSQLManager sqlm, AbstractQueries qrs) {
        this.sqlm = sqlm;
        sqlm.setQueries(qrs);
    }

    /**
     * Gets already confiugred instance and delivers it to this class.
     *
     * @param sqlm AbstractSQLManager instance realization with pre-configured Queries
     * @since 1.3
     */
    @Override
    public void deliverDBProvider(AbstractSQLManager sqlm) {
        this.sqlm = sqlm;
    }

    /**
     * Get the provider of data fetching utilities. We made this abstraction to be able to create a fork for other
     * universities.
     *
     * @param df a created instance of AbstractDataFetcher implementation (for instance, SSUDataFetcher).
     * @since 1.1
     */
    @Override
    public void deliverDataFetcherProvider(AbstractDataFetcher df) {
        this.df = df;
    }

    /**
     * Gets the provider of data representation module. It can be json or yaml or xml, but in TT Core only JSON implemented.
     *
     * @param dconv a created instance of AbstractDataConverter implementation (for instance, JSONConverter)
     * @see org.tt.core.dm.AbstractDataConverter
     * @see org.tt.core.dm.convert.json.JSONConverter
     * @since 1.2
     */
    @Override
    public void deliverDataConverterProvider(AbstractDataConverter dconv) {
        this.dconv = dconv;
    }

    /**
     * Providing URL to fetch data from.
     *
     * @param globalURLString Global schedule URL to use in data fetching
     * @since 1.3
     */
    @Override
    public void deliverGlobalURL(String globalURLString) {
        scheduleURL = globalURLString;
    }

    public JSONConverter getJSONConverter() {
        return (JSONConverter) dconv;
    }
}
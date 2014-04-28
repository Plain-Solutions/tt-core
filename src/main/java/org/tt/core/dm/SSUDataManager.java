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

import org.tt.core.entity.datamanager.TTData;
import org.tt.core.entity.datamanager.TTStatus;
import org.tt.core.fetch.AbstractDataFetcher;
import org.tt.core.entity.datafetcher.Department;
import org.tt.core.entity.datafetcher.Group;
import org.tt.core.entity.datafetcher.Lesson;
import org.tt.core.sql.AbstractQueries;
import org.tt.core.sql.AbstractSQLManager;
import org.tt.core.entity.db.TTEntity;
import org.tt.core.sql.H2Queries;
import org.tt.core.sql.ex.NoSuchDepartmentException;
import org.tt.core.sql.ex.NoSuchGroupException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * SSUDataManager is a implementation of AbstractDataManager which works with SSU schedule page.
 * It connects:
 * <ul>
 * <li>SSUSQLManager - database connection interface</li>
 * <li>LexxDataFetcher or similar - utilities to parse SSU timetable input (HTML pages, XML DB replica)</li>
 * </ul>
 * However, the main aim of AbstractDataManager is to manage data, especially raw data - text strings and collections of them.
 * We are trying to keep this idea working and handle string and sometimes some structures, where it seems easy,
 * correct and what it important for TT Platform - JSON-friendly.
 *
 * @author Vlad Slepukhin, Nickolay Yurin
 * @since 1.0
 */

public class SSUDataManager implements AbstractDataManager {
    protected AbstractDataFetcher df;
    protected AbstractSQLManager sqlm;
    private AbstractDataConverter dconv;

    /**
     * Constructor with no params now is empty.
     */
    public SSUDataManager() {
    }

    /**
     * Constructor with all provided datafetcher of data suppliers.
     *
     * @param sqlm  SQLManager instance.
     * @param qrs   Queries for SQL instance.
     * @param df    DataFetcher instance.
     * @param dconv DataConverter instance.
     * @see org.tt.core.sql.AbstractSQLManager
     * @see org.tt.core.sql.AbstractQueries
     * @see org.tt.core.fetch.AbstractDataFetcher
     * @see org.tt.core.dm.AbstractDataConverter
     * @since 1.2
     */
    public SSUDataManager(AbstractSQLManager sqlm,
                          AbstractQueries qrs,
                          AbstractDataFetcher df,
                          AbstractDataConverter dconv
    ) {
        deliverDBProvider(sqlm, qrs);
        deliverDataFetcherProvider(df);
        deliverDataConverterProvider(dconv);
    }

    @Override
    public TTData initUpdateJobs() {
        //here should be job creator;
        //but now we put here test functions
        //UpdateManager will be called from Timer!!
        UpdateManager updm = new UpdateManager(sqlm, new H2Queries(), df, dconv);
        try {
//            updm.checkDepartments();
            //updm.checkGroups();
            updm.checkTimetables();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchDepartmentException e) {
            e.printStackTrace();
        } catch (NoSuchGroupException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Connect AbstractDataFetcher to get departments map and store it into DB with AbstractSQLManager.
     *
     * @return TTData
     * @see org.tt.core.dm.AbstractDataConverter
     * @see org.tt.core.fetch.AbstractDataFetcher
     * @since 1.0
     */
    @Override
    public TTData putDepartments() {
        TTData result = new TTData();
        try {
            sqlm.putDepartments(df.getDepartments());
            result.setHttpCode(200);
            result.setMessage(dconv.convertStatus(TTStatus.OK, TTStatus.OKMSG));
        } catch (SQLException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.GENSQL, e.getSQLState()));
        }
        System.out.println("added departments");
        return result;
    }

    /**
     * Connect AbstractDataFetcher to get list of groups on <b>department</b> and store it into DB with AbstractSQLManager.
     *
     * @param departmentTag the tag (token) of the department.
     * @return TTData
     * @see org.tt.core.dm.AbstractDataConverter
     * @see org.tt.core.fetch.AbstractDataFetcher
     * @since 1.0
     */
    @Override
    public TTData putDepartmentGroups(String departmentTag) {
        TTData result = new TTData();
        try {
            sqlm.putGroups(df.getGroups(departmentTag), departmentTag);
            result.setHttpCode(200);
            result.setMessage(dconv.convertStatus(TTStatus.OK, TTStatus.OKMSG));
        } catch (SQLException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.GENSQL, e.getSQLState()));
        } catch (NoSuchDepartmentException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.TTSQL, TTStatus.DEPARTMENTERR));
        }
        System.out.println(String.format("added %s groups", departmentTag));
        return result;
    }

    /**
     * Connect AbstractDataFetcher to get list of <b>ALL</b> groups and store it into DB.
     *
     * @return TTData
     * @see org.tt.core.dm.AbstractDataConverter
     * @see org.tt.core.fetch.AbstractDataFetcher
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
     * Adding temporary table from AbstractDataFetcher in a proper format to DB.
     *
     * @param departmentTag the tag of the department, where the group is located (allocation check).
     * @param groupName     the name of the group
     * @return TTData with <code>httpCode</code> 200
     * <code>module:ok</code> and empty message String in case of success or error trace.
     * @since 2.0
     */
    @Override
    public TTData putTT(String departmentTag, String groupName) {
        TTData result = new TTData();
        try {
            int groupID = sqlm.getGroupID(departmentTag, groupName);

            int day = 1;
            List<List<Lesson>> timetable = df.getTT(departmentTag, groupName);


            for (List<Lesson> ls : timetable) {
                for (Lesson l : ls) {
                    if (!l.isEmpty()) {
                        int sequence = l.getSequence();
                        int parityID = sqlm.getParityID(l.getParity());
                        int subgrpID = sqlm.putSubGroup(groupID, l.getSubgroup());
                        int activityID = sqlm.getActivityID(l.getActivity());
                        int subjectID = sqlm.putSubject(l.getSubject());
                        int teacherID = sqlm.putTeacher(l.getTeacher());
                        int locationID = sqlm.putLocation(l.getBuilding(), l.getRoom());
                        int datatimeID = sqlm.putDateTime(parityID, sequence, day);


                        sqlm.putLessonRecord(groupID, datatimeID, activityID, subjectID, subgrpID, teacherID,
                                locationID, l.getTimestamp());
                    }
                }
                day++;

            }
            System.out.println(String.format("added timetable for  %s@%s", groupName, departmentTag));
            result.setHttpCode(200);
            result.setMessage(dconv.convertStatus(TTStatus.OK, TTStatus.OKMSG));
        } catch (SQLException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.GENSQL, e.getSQLState()));
        } catch (NoSuchGroupException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.TTSQL, TTStatus.GROUPERR));
        } catch (NoSuchDepartmentException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.TTSQL, TTStatus.DEPARTMENTERR));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Adding temporary table of all groups in all the departments from AbstractDataFetcher in a proper format to DB.
     *
     * @return TTData with <code>httpCode</code> 200
     * <code>module:ok</code> and empty message String in case of success or error trace.
     * @since 2.0
     */
    @Override
    public TTData putAllTT() {
        TTData result = new TTData();
        try {
            for (String d : sqlm.getDepartmentTags()) {
                for (Group grp : sqlm.getGroups(d))
                    putTT(d, grp.getName());
            }
            result.setHttpCode(200);
            result.setMessage(dconv.convertStatus(TTStatus.OK, TTStatus.OKMSG));
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
     * Get the map of stored departments.
     *
     * @return TTData instance with JSON-formatted String and success/error code.
     * @see org.tt.core.dm.convert.json.serializer.DepartmentSerializer
     * @since 2.0
     */
    @Override
    public TTData getDepartments() {
        TTData result = new TTData();
        try {
            List<Department> raw = sqlm.getDepartments();
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
     * @see org.tt.core.dm.convert.json.JSONConverter
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
     * Gets displayable group names (151, 451) as list.
     *
     * @param departmentTag the tag of the department.
     * @return JSON List of tags.
     * @see org.tt.core.dm.convert.json.serializer.GroupListSerializer
     * @since 2.0
     */
    @Override
    public TTData getGroups(String departmentTag) {
        TTData result = new TTData();
        try {
            List<Group> raw = sqlm.getGroups(departmentTag);
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
     * Gets displayable group names (151, 451) which have timetables in the database as list.
     *
     * @param departmentTag the tag of the department.
     * @return JSON List of tags.
     * @see org.tt.core.dm.convert.json.serializer.GroupListSerializer
     * @since 2.0
     */
    @Override
    public TTData getNonEmptyGroups(String departmentTag) {
        TTData result = new TTData();
        try {
            List<Group> raw = sqlm.getNonEmptyGroups(departmentTag);
            result.setHttpCode(200);
            result.setMessage(dconv.convertGroupList(raw));
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
     * Gets already configured instance and delivers it to this class.
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
     * Get nearly raw, but formatted timetable output from the database and process it to some web-friendly format.
     *
     * @param groupID the global id of group to get the timetable.
     * @return TTData instance with JSON-formatted String and success/error code.
     * @see org.tt.core.dm.convert.json.serializer.TimeTableSerializer
     * @since 1.1
     */
    private TTData getTT(int groupID) {
        TTData result = new TTData();

        try {
            TTEntity raw = sqlm.getTT(groupID);
            result.setHttpCode(200);
            result.setMessage(dconv.convertTT(raw));
        } catch (SQLException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.GENSQL, e.getSQLState()));
        } catch (NoSuchGroupException e) {
            result.setHttpCode(404);
            result.setMessage(dconv.convertStatus(TTStatus.TTSQL, TTStatus.GROUPERR));
        }

        return result;
    }

}

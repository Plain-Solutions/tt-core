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

import org.quartz.SchedulerException;
import org.tt.core.entity.datafetcher.Department;
import org.tt.core.entity.datafetcher.Group;
import org.tt.core.entity.datafetcher.Lesson;
import org.tt.core.entity.datamanager.TTData;
import org.tt.core.entity.datamanager.TTStatus;
import org.tt.core.entity.db.TTEntity;
import org.tt.core.fetch.AbstractDataFetcher;
import org.tt.core.sql.AbstractQueries;
import org.tt.core.sql.AbstractSQLManager;
import org.tt.core.sql.H2Queries;
import org.tt.core.sql.ex.NoSuchDepartmentException;
import org.tt.core.sql.ex.NoSuchGroupException;
import org.tt.core.timer.AbstractJob;
import org.tt.core.timer.TTTimer;
import org.tt.core.timer.jobs.JobDrop;
import org.tt.core.timer.jobs.JobUpdate;

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
                          AbstractDataFetcher df
    ) {
        deliverDBProvider(sqlm, qrs);
        deliverDataFetcherProvider(df);
    }

    @Override
    public void initUpdateJobs() throws SchedulerException {
        UpdateManager updm = new UpdateManager(sqlm, new H2Queries(), df);
        AbstractJob.setUpdateManager(updm);

        AbstractJob updateJob = new JobUpdate();
        AbstractJob dropJob = new JobDrop();


        TTTimer tm = TTTimer.getInstance(dropJob, updateJob);
        tm.start();
        System.out.println("Next update: " + updateJob.getTrigger().getNextFireTime().toString());
        System.out.println("Next drop: " + dropJob.getTrigger().getNextFireTime().toString());
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
    public void putDepartments() throws SQLException {
        sqlm.putDepartments(df.getDepartments());
        System.out.println("added departments");
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
    public void putDepartmentGroups(String departmentTag) throws NoSuchDepartmentException, SQLException {
        sqlm.putGroups(df.getGroups(departmentTag), departmentTag);

        System.out.println(String.format("added %s groups", departmentTag));
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
    public void putAllGroups() throws NoSuchDepartmentException, SQLException {
        for (String departmentTag : sqlm.getDepartmentTags())
            putDepartmentGroups(departmentTag);

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
    public void putTT(String departmentTag, String groupName) throws SQLException, NoSuchDepartmentException, NoSuchGroupException, IOException {
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
    }

    /**
     * Adding temporary table of all groups in all the departments from AbstractDataFetcher in a proper format to DB.
     *
     * @return TTData with <code>httpCode</code> 200
     * <code>module:ok</code> and empty message String in case of success or error trace.
     * @since 2.0
     */
    @Override
    public void putAllTT() throws SQLException, NoSuchDepartmentException, IOException, NoSuchGroupException {
        for (String d : sqlm.getDepartmentTags()) {
            for (Group grp : sqlm.getGroups(d))
                putTT(d, grp.getName());
        }
    }


    /**
     * Get the map of stored departments.
     *
     * @return TTData instance with JSON-formatted String and success/error code.
     * @see org.tt.core.dm.convert.json.serializer.DepartmentSerializer
     * @since 2.0
     */
    @Override
    public List<Department> getDepartments() throws SQLException {
        return sqlm.getDepartments();
    }

    /**
     * Get list of the tags for each department. TT Platform&Servlets are tied around them.
     *
     * @return JSON List of department tags.
     * @see org.tt.core.dm.convert.json.JSONConverter
     * @since 1.0
     */
    @Override
    public List<String> getDepartmentTags() throws SQLException {
        return sqlm.getDepartmentTags();
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
    public List<Group> getGroups(String departmentTag) throws NoSuchDepartmentException, SQLException {
        return sqlm.getGroups(departmentTag);
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
    public List<Group> getNonEmptyGroups(String departmentTag) throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
       return sqlm.getNonEmptyGroups(departmentTag);
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
    public TTEntity getTT(String departmentTag, String groupName) throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
            return getTT(sqlm.getGroupID(departmentTag, groupName));
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


    /**
     * Get nearly raw, but formatted timetable output from the database and process it to some web-friendly format.
     *
     * @param groupID the global id of group to get the timetable.
     * @return TTData instance with JSON-formatted String and success/error code.
     * @see org.tt.core.dm.convert.json.serializer.TimeTableSerializer
     * @since 1.1
     */
    private TTEntity getTT(int groupID) throws SQLException, NoSuchGroupException {

            return sqlm.getTT(groupID);
    }

}

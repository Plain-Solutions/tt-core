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

import org.ssutt.core.fetch.AbstractDataFetcher;
import org.ssutt.core.sql.AbstractQueries;
import org.ssutt.core.sql.AbstractSQLManager;

/**
 * AbstractDataManager is the main external interface of TT Core Library. Actually, it represents a connector
 * between all the internal modules and external interface.
 * <p/>
 * It should be connected to TT Platform to deliver information to end-user, but some modules (AbstractDataFetcher)
 * work independently from this connection.
 *
 * @author Vlad Slepukhin
 * @since 1.0
 */
public interface AbstractDataManager {

    /**
     * Fetch and put departments list with connected information to the database.
     *
     * @return TTData with <code>httpCode</code> 200
     * <code>module:ok</code> and empty message String in case of success or error trace.
     */
    TTData putDepartments();

    /**
     * Connect AbstractDataFetcher to get list of groups on <b>department</b> and store it into DB with AbstractSQLManager.
     *
     * @param departmentTag the tag of the department.
     * @return TTData with <code>httpCode</code> 200
     * <code>module:ok</code> and empty message String in case of success or error trace.
     */
    TTData putDepartmentGroups(String departmentTag);

    /**
     * Connect AbstractDataFetcher to get list of <b>ALL</b> groups and store it into DB.
     *
     * @return TTData with <code>httpCode</code> 200
     * <code>module:ok</code> and empty message String in case of success or error trace.
     * @since 1.0
     */
    TTData putAllGroups();

    /**
     * Adding temporary table from AbstractDataFetcher (parsed by HTML tags table) in a proper format to DB.
     *
     * @param departmentTag the tag of the department, where the group is located (allocation check).
     * @param groupID       the global id of group.
     * @return TTData with <code>httpCode</code> 200
     * <code>module:ok</code> and empty message String in case of success or error trace.
     * @since 1.0
     */
    TTData putTT(String departmentTag, int groupID);

    /**
     * Get the map of stored departments.
     *
     * @return TTData instance with JSON-formatted String and success/error code.
     * @see org.ssutt.core.dm.convert.json.serializer.DepartmentSerializer
     * @since 1.1
     */
    TTData getDepartments();

    /**
     * Get list of the tags for each department. TT Platform&Servlets are tied around them.
     *
     * @return JSON List of department tags.
     * @since 1.0
     */
    TTData getDepartmentTags();

    /**
     * Get displayable group names (151, 451) as Strings (see non-numerical groups in
     * {@link org.ssutt.core.fetch.SSUDataFetcher})on specified department.
     *
     * @param departmentTag the tag of the department.
     * @return JSON List of tags.
     * @since 1.0
     */
    TTData getGroups(String departmentTag);

    /**
     * Converts name of group of the specified department (allocation check, throws NoSuchGroupException) to its global
     * id in the database. Used by TT Platform&Servlets.
     *
     * @param departmentTag the tag of the department.
     * @param groupName     the displayable name.
     * @return TTData instance with JSON-formatted String and success/error code.
     * @since 1.0
     */
    TTData getGroupID(String departmentTag, String groupName);

    /**
     * Get nearly raw, but formatted timetable output from the database and process it to some web-friendly format.
     *
     * @param groupID the global id of group to get the timetable.
     * @return TTData instance with JSON-formatted String and success/error code.
     * @see org.ssutt.core.dm.convert.json.serializer.TimeTableSerializer
     * @since 1.1
     */
    TTData getTT(int groupID);

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
    void deliverDBProvider(AbstractSQLManager sqlm, AbstractQueries qrs);

    /**
     * Get the provider of data fetching utilities. We made this abstraction to be able to create a fork for other
     * universities.
     *
     * @param df a created instance of AbstractDataFetcher implementation (for instance, SSUDataFetcher).
     * @since 1.1
     */
    void deliverDataFetcherProvider(AbstractDataFetcher df);

    /**
     * Gets the provider of data representation module. It can be json or yaml or xml, but in TT Core only JSON implemented.
     *
     * @param dconv a created instance of AbstractDataConverter implementation (for instance, JSONConverter)
     * @see org.ssutt.core.dm.AbstractDataConverter
     * @see org.ssutt.core.dm.convert.json.JSONConverter
     * @since 1.2
     */
    void deliverDataConverterProvider(AbstractDataConverter dconv);
}
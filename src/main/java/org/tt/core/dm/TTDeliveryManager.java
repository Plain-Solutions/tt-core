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

import org.tt.core.entity.datafetcher.Department;
import org.tt.core.entity.datafetcher.Group;
import org.tt.core.entity.db.TTEntity;
import org.tt.core.sql.AbstractSQLManager;
import org.tt.core.sql.ex.NoSuchDepartmentException;
import org.tt.core.sql.ex.NoSuchGroupException;

import java.sql.SQLException;
import java.util.List;

/**
 * TTDeliveryManager - a class, handling distributing data to TT Platform from the database. In fact it is
 * a layer between SQL and some client. Actually, our main highway to the web. It is the result of split
 * AbstractDataManager from 1.0-1.2.x and 2.0 versions of TT Core.
 *
 * @author Vlad Slepukhin
 * @since 2.1.0
 */
public class TTDeliveryManager {
    /**
     * Pre-configured instance of AbstractSQLManager. It should contain configured ASQLM instance with initialised
     * AbstractQueries instance.
     */
    private AbstractSQLManager sqlm;

    /**
     * Constructor with SQL Manager instance.
     *
     * @param sqlm {@link org.tt.core.sql.AbstractSQLManager} instance with initialised {@link org.tt.core.sql.AbstractQueries}
     *             instance.
     * @since 2.1.0
     */
    public TTDeliveryManager(AbstractSQLManager sqlm) {
        this.sqlm = sqlm;
    }

    /**
     * Get list of departments from the database. Used in <code>/departments</code> call.
     *
     * @return List of {@link org.tt.core.entity.datafetcher.Department} containing all data: names, tags and messages.
     * @throws SQLException
     * @since 2.1.0
     */
    public List<Department> getDepartments() throws SQLException {
        return sqlm.getDepartments();
    }

    /**
     * Get list of tags - short abbreviations of departments' names to use them in API calls (bf, knt or similar).
     *
     * @return List of Strings.
     * @throws SQLException
     * @since 2.1.0
     */
    public List<String> getDepartmentTags() throws SQLException {
        return sqlm.getDepartmentTags();
    }

    /**
     * Get the department message - some crucial data provided by SSU: activities, odd/even weeks calendar, additional
     * classes. Used in <code>/department/< tag >/msg</code> call.
     *
     * @param departmentTag The department abbreviation.
     * @return The message.
     * @throws SQLException              in case of general SQL error.
     * @throws NoSuchDepartmentException in case of wrong query.
     * @since 2.1.0
     */
    public String getDepartmentMessage(String departmentTag) throws SQLException, NoSuchDepartmentException {
        return sqlm.getDepartmentMessage(departmentTag);
    }

    /**
     * Get the list of all existing groups. Used in <code>/departments/< tag >/groups/all</code> call.
     *
     * @param departmentTag The department abbreviation.
     * @return List of {@link org.tt.core.entity.datafetcher.Group}.
     * @throws SQLException
     * @throws NoSuchDepartmentException in case of wrong query.
     * @since 2.1.0
     */
    public List<Group> getGroups(String departmentTag) throws SQLException, NoSuchDepartmentException {
        return sqlm.getGroups(departmentTag);
    }

    /**
     * Get the list of only groups, which have timetables in the database.
     * Used in <code>/departments/< tag >/groups/nonemp</code>
     *
     * @param departmentTag The department abbreviation.
     * @return List of {@link org.tt.core.entity.datafetcher.Group}.
     * @throws SQLException
     * @throws NoSuchDepartmentException in case of wrong query.
     * @throws NoSuchGroupException      a <i>dependency</i> (internal used methods) exception.
     * @since 2.1.0
     */
    public List<Group> getNonEmptyGroups(String departmentTag) throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
        return sqlm.getNonEmptyGroups(departmentTag);
    }

    /**
     * Get the timetable in a internal format. Used in <code>/departments/< tag >/group/< name ></code>
     *
     * @param departmentTag The department abbreviation.
     * @param groupName     The name of the group.
     * @return a prepared instance of {@link org.tt.core.entity.db.TTEntity}
     * @throws SQLException              in case of general SQL error.
     * @throws NoSuchDepartmentException in case of wrong query.
     * @throws NoSuchGroupException      in case of wrong query.
     * @since 2.0.0
     */
    public TTEntity getTT(String departmentTag, String groupName) throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
        return getTT(sqlm.getGroupID(departmentTag, groupName));
    }

    /**
     * A chain method, legacy from first version of TT. Still provides better architecture as groups have IDs.
     *
     * @param groupID The ID of the group.
     * @return a prepared instance of {@link org.tt.core.entity.db.TTEntity}
     * @throws SQLException
     * @throws NoSuchGroupException in case of wrong query.
     * @since 1.0.0
     */
    private TTEntity getTT(int groupID) throws SQLException, NoSuchGroupException {
        return sqlm.getTT(groupID);
    }
}

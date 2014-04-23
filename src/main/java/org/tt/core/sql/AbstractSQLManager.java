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
package org.tt.core.sql;

import org.tt.core.fetch.lexx.entity.Department;
import org.tt.core.fetch.lexx.entity.Group;
import org.tt.core.sql.ex.EmptyTableException;
import org.tt.core.sql.ex.NoSuchDepartmentException;
import org.tt.core.sql.ex.NoSuchGroupException;

import java.sql.SQLException;
import java.util.List;

/**
 * AbstractSQLManager is one the core workers in TT Core library that serves as a bridge
 * between raw data, supplied by AbstractDataFetcher and AbstractDataManager and some database instance, which
 * description is usually passed by AbstractDataManager.
 * <p/>
 * There is no need to describe each prototype in detail, so please follow 'see' link:
 *
 * @author Vlad Slepukhin
 * @see org.tt.core.sql.SSUSQLManager
 * @since 1.0
 */
public interface AbstractSQLManager {
    void putDepartments(List<Department> departments) throws SQLException;

    void putGroups(List<Group> groups, String departmentTag) throws SQLException, NoSuchDepartmentException;

    int putDateTime(int weekID, int sequence, int dayID) throws SQLException;

    int putSubject(String info) throws SQLException;

    int putLocation(String building, String room) throws SQLException;

    int putTeacher(String name) throws  SQLException;

    int putSubGroup(int groupID, String name) throws SQLException;

    void putLessonRecord(int groupID, int dateTimeID, int activityID, int subjectID, int subGroupID, int teacherID,
                         int locationID, long timestamp) throws SQLException;


    List<Department> getDepartments() throws SQLException;

    List<String> getDepartmentTags() throws SQLException;

    List<Group> getGroups(String departmentTag) throws SQLException, NoSuchDepartmentException;

    List<Group> getNonEmptyGroups(String departmentTag) throws SQLException, NoSuchDepartmentException, NoSuchGroupException;

    int getGroupID(String departmentTag, String groupName) throws SQLException,
            NoSuchDepartmentException, NoSuchGroupException;

    String getGroupName(String departmentTag, int groupID) throws SQLException,
            NoSuchDepartmentException, NoSuchGroupException;

    List<List<DBLesson>> getTT(int groupID) throws SQLException, NoSuchGroupException, EmptyTableException;


    boolean departmentExists(String departmentTag) throws SQLException;

    boolean groupExistsInDepartment(String departmentTag, String groupName) throws SQLException;

    boolean groupExistsAsID(int groupID) throws SQLException;

    boolean lessonExists(int groupID, int dateTimeID, int subjectID) throws SQLException;

    boolean groupHasTable(int groupID) throws SQLException;

    int getLastID(String table) throws SQLException;

    int getParityID (String state) throws SQLException;

    int getActivityID (String type) throws SQLException;

    void setQueries(AbstractQueries qrs);

}

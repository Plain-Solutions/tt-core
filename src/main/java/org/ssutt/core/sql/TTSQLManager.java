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
package org.ssutt.core.sql;

import org.ssutt.core.sql.ex.EmptyTableException;
import org.ssutt.core.sql.ex.NoSuchDepartmentException;
import org.ssutt.core.sql.ex.NoSuchGroupException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * TTSQLManager is one the core workers in TT Core library that serves as a bridge
 * between raw data, supplied by TTDataFetcher and TTDataManager and some database instance, which
 * description is usually passed by TTDataManager.
 */
public interface TTSQLManager {
    void putDepartments(Map<String, String> departments) throws SQLException;

    void putGroups(List<String> groups, String departmentTag) throws SQLException, NoSuchDepartmentException;

    int putDateTime(int weekID, int sequence, int dayID) throws SQLException;

    int putSubject(String info) throws SQLException;

    void putLessonRecord(int groupID, int dateTimeID, int subjectID) throws SQLException;


    Map<String, String> getDepartments() throws SQLException;

    List<String> getDepartmentTags() throws SQLException;

    List<String> getGroups(String departmentTag) throws SQLException, NoSuchDepartmentException;

    int getGroupID(String departmentTag, String groupName) throws SQLException,
            NoSuchDepartmentException, NoSuchGroupException;

    String getGroupName(String departmentTag, int groupID) throws SQLException,
            NoSuchDepartmentException, NoSuchGroupException;

    List<String[]> getTT(int groupID) throws SQLException, NoSuchGroupException, EmptyTableException;


    boolean departmentExists(String departmentTag) throws SQLException;

    boolean groupExistsInDepartment(String departmentTag, String groupName) throws SQLException;

    boolean groupExistsAsID(int groupID) throws SQLException;

    boolean lessonExists(int groupID, int dateTimeID, int subjectID) throws SQLException;

    int getLastID(String table) throws SQLException;

}

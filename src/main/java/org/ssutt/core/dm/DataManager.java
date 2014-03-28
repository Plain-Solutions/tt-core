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

import org.ssutt.core.dm.entities.TableEntity;
import org.ssutt.core.sql.ex.EmptyTableException;
import org.ssutt.core.sql.ex.NoSuchDepartmentException;
import org.ssutt.core.sql.ex.NoSuchGroupException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DataManager {
    void deliverDBProvider(Connection conn);

    void deliverDataFetcherProvider();


    void putDepartments() throws SQLException;

    void putDepartmentGroups(String departmentTag) throws  SQLException, NoSuchDepartmentException;

    void putAllGroups() throws SQLException, NoSuchDepartmentException;

    void putTT(String departmentTag, int groupID) throws IOException, SQLException,
            NoSuchDepartmentException, NoSuchGroupException;

    Map<String, String> getDepartments() throws SQLException;

    List<String> getDepartmentTags() throws SQLException;

    List<String> getGroups(String departmentTag) throws SQLException, NoSuchDepartmentException;

    int getGroupID(String departmentTag, String groupName) throws SQLException, NoSuchDepartmentException,
            NoSuchGroupException;

    TableEntity getTT(int groupID) throws SQLException, NoSuchGroupException, EmptyTableException;

}

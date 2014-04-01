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
import org.ssutt.core.sql.ex.NoSuchDepartmentException;
import org.ssutt.core.sql.ex.NoSuchGroupException;

import java.io.IOException;
import java.sql.SQLException;

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

    void putDepartments() throws SQLException;

    void putDepartmentGroups(String departmentTag) throws SQLException, NoSuchDepartmentException;

    void putAllGroups() throws SQLException, NoSuchDepartmentException;

    void putTT(String departmentTag, int groupID) throws IOException, SQLException,
            NoSuchDepartmentException, NoSuchGroupException;

    TTData getDepartments();

    TTData getDepartmentTags();

    TTData getGroups(String departmentTag);

    TTData getGroupID(String departmentTag, String groupName);

    TTData getTT(int groupID);


    void deliverDBProvider(AbstractSQLManager sqlm, AbstractQueries qrs);

    void deliverDataFetcherProvider(AbstractDataFetcher df);

    void deliverDataConverterProvider(AbstractDataConverter dconv);
}

/*
Copyright 2014 Plain Solutions

Authors:
    Vlad Slepukhin <slp.vld@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.ssutt.core.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.TreeSet;


/**
 * Manages data in h2 database and
 * executes queries
 */
public class SSUSQLManager implements SQLManager {
    private static Connection conn;

    /**
     * Creates an instance of SQLManager for the library user
     *
     * @param java.sql.Connection conn - connection from JNDI from API module
     * @return org.ssutt.core.sql.SSUSQLManager instance (should be singleton some day)
     */
    public SSUSQLManager(Connection conn) {
        SSUSQLManager.conn = conn;
        logger.info("SSU SQLManager initialized with JNDI DB (H2) correctly.");
    }



    @Override
    public String getDepartmentTag(String name) {
        return null;
    }

    @Override
    public String getDepartmentName(String tag) {
        return null;
    }

    @Override
    public HashMap<String, String> getDepartments() {
        return null;
    }

    @Override
    public String getGroups(String departmentCode) {
        return null;
    }

    @Override
    public String getTimetable(String departmentCode, String groupName) {
        return null;
    }

    @Override
    public boolean fillDepartments(HashMap<String, String> departments) {
        boolean state = true;
        for (String key: new TreeSet<>(departments.keySet()))
        {
            if (pushExecutioner(
                    String.format(Queries.putDepartments,
                            key,
                            departments.get(key))))
                state = true;
            else state = false;
        }
        return state;
    }

    @Override
    public boolean pushExecutioner(String query) {
        boolean isScriptExecuted = false;
        try (Statement stmt = conn.createStatement();) {
            stmt.executeUpdate(query);
            stmt.close();
        }   catch (SQLException e) {
            logger.fatal("On query: "+ e);
        }

        return isScriptExecuted;
    }
}

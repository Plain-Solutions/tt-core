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
package org.ssutt.core.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


/**
 * Manages data in h2 database and
 * executes queries
 */
public class SSUSQLManager implements SQLManager {
    static final Logger logger = LogManager.getLogger(SQLManager.class.getName());
    private static Connection conn;
    private static Queries qrs;

    public SSUSQLManager(Connection conn) {
        SSUSQLManager.conn = conn;
        qrs = new H2Queries();

        logger.info("SSU SQLManager initialized with JNDI DB (H2) correctly.");
    }


    @Override
    public void putDepartments(Map<String, String> departments) throws SQLException {
        Statement stmt = conn.createStatement();

        for (String d : new TreeSet<>(departments.keySet())) {
            stmt.executeUpdate(String.format(qrs.qAddDepartment(), d, departments.get(d)));
        }

        stmt.close();
    }

    @Override
    public Map<String, String> getDepartments() throws SQLException {
        Map<String, String> result = new HashMap<>();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(qrs.qGetDepartments());

        while (rs.next()) {
            result.put(rs.getString("name"), rs.getString("tag"));
        }
        return result;
    }

    @Override
    public List<String> getDepartmentTags() throws SQLException {
        List<String> result = new ArrayList<>();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(qrs.qGetDepartmentTags());

        while (rs.next())
            result.add(rs.getString("tag"));

        return result;
    }

    @Override
    public boolean departmentExists(String departmentTag) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qDepartmentExists(), departmentTag));
        int id = 0;
        while (rs.next()) id = rs.getInt("id");

        return (id != 0) ? true : false;
    }

    @Override
    public void putGroups(List<String> groups, String departmentTag) throws SQLException {
        if (departmentExists(departmentTag)) {
            Statement stmt = conn.createStatement();
            Collections.sort(groups);
            for (String g : groups) {
                stmt.executeUpdate(String.format(qrs.qAddGroups(), departmentTag, g));
            }

            stmt.close();
        }
        else throw new SQLException("no department found with such tag");
    }

    @Override
    public List<String> getGroups(String departmentTag) throws SQLException {
        if (departmentExists(departmentTag)) {
            List<String> result = new ArrayList<>();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(String.format(qrs.qGetGroups(), departmentTag));

            while (rs.next())
                result.add(rs.getString("name"));
            return result;
        }

        return null;
    }

    @Override
    public boolean groupExists(String departmentTag, String groupName) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qGroupExists(), departmentTag, groupName));
        int id = 0;
        while (rs.next()) id = rs.getInt("id");

        return (id != 0) ? true : false;
    }

}

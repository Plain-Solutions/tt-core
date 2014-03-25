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

    public SSUSQLManager(Connection conn) {
        SSUSQLManager.conn = conn;
        logger.info("SSU SQLManager initialized with JNDI DB (H2) correctly.");
    }


    @Override
    public void putDepartments(Map<String, String> departments) throws SQLException {
        Statement stmt = conn.createStatement();
        String addDepartment = "INSERT INTO departments(name,tag) VALUES('%s','%s');";
        for (String d : new TreeSet<>(departments.keySet())) {
            stmt.executeUpdate(String.format(addDepartment, d, departments.get(d)));
        }
        stmt.close();
    }

    @Override
    public Map<String, String> getDepartments() throws SQLException {
        Map<String, String> result = new HashMap<>();

        Statement stmt = conn.createStatement();
        String getDepartments = "SELECT name,tag FROM departments";
        ResultSet rs = stmt.executeQuery(getDepartments);

        while (rs.next()) {
            result.put(rs.getString("name"), rs.getString("tag"));
        }
        return result;
    }

    @Override
    public List<String> getDepartmentTags() throws SQLException {
        List<String> result = new ArrayList<>();
        Statement stmt = conn.createStatement();
        String getTags = "SELECT tag FROM departments";

        ResultSet rs = stmt.executeQuery(getTags);
        while (rs.next())
            result.add(rs.getString("tag"));
        return result;
    }

    @Override
    public void putGroups(Map<String, String> groups, String department) throws SQLException {
        Statement stmt = conn.createStatement();
        String addGroups = "INSERT INTO GROUPS(department_id, name, unesc) VALUES" +
        "((SELECT id FROM DEPARTMENTS WHERE tag='%s'),'%s', '%s'); ";

        for (String g : new TreeSet<>(groups.keySet())) {
            stmt.executeUpdate(String.format(addGroups, department,g,groups.get(g)));
        }
        stmt.close();

    }

    @Override
    public Map<String, String> getGroups(String departmentTag) throws SQLException {
        Map<String,String> result = new HashMap<>();
        Statement stmt = conn.createStatement();
        String getGroups = "SELECT gr.name, gr.unesc FROM groups as gr, departments as dp WHERE gr.department_id = dp.id AND dp.tag = '%s';";
        ResultSet rs = stmt.executeQuery(String.format(getGroups,departmentTag));

        while (rs.next())
            result.put(rs.getString("name"),rs.getString("unesc"));
        return result;

    }

}

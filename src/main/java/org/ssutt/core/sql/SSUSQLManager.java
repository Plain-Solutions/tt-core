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

import org.ssutt.core.sql.ex.EmptyTableException;
import org.ssutt.core.sql.ex.NoSuchDepartmentException;
import org.ssutt.core.sql.ex.NoSuchGroupException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


/**
 * SSUSQLManager is an implementation of TTSQLManager, configured for usage
 * in SSU with H2DB (defined in the constructor)
 *
 * @author Vlad Slepukhin
 * @since 1.0
 */
public class SSUSQLManager implements TTSQLManager {
    private static Connection conn;
    private static Queries qrs;

    /**
     * Constructor that creates configured TTSQLManager instance for usage in TTDataManager (mainly)
     * @param conn java.sql.Connection of database provider, that should be familiar to instance creator method
     */
    public SSUSQLManager(Connection conn) {
        SSUSQLManager.conn = conn;
    }

    /**
     * Adds departments to the DB.
     * @param departments Map of departments: name-tag. Better to be sorted by displayed name.  
     * @throws SQLException
     */
    @Override
    public void putDepartments(Map<String, String> departments) throws SQLException {
        Statement stmt = conn.createStatement();

        for (String d : new TreeSet<>(departments.keySet())) {
            stmt.executeUpdate(String.format(qrs.qAddDepartment(), d, departments.get(d)));
        }

        stmt.close();
    }

    /**
     * Adds groups to the DB.
     * @param groups list of group names or numbers, that will be displayed to the end-user.
     * @param departmentTag tag of the department to associate with the group in <code>groups</code> table.
     * @throws SQLException
     * @throws NoSuchDepartmentException If no such department found. 
     */
    @Override
    public void putGroups(List<String> groups, String departmentTag) throws SQLException, NoSuchDepartmentException {
        if (departmentExists(departmentTag)) {
            Statement stmt = conn.createStatement();
            Collections.sort(groups);
            for (String g : groups) {
                stmt.executeUpdate(String.format(qrs.qAddGroups(), departmentTag, g));
            }

            stmt.close();
        } else throw new NoSuchDepartmentException();
    }

    /**
     * Adds datetime information about lesson to <code>lessons_datetimes</code> table, if no such datetime found.
     * @param weekID identifier from week_states: even, odd or all.
     * @param sequence the order of the lesson during the day.
     * @param dayID day number in the week, Monday - 1.
     * @return id of the datetime record.
     * @throws SQLException
     */
    @Override
    public int putDateTime(int weekID, int sequence, int dayID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qGetDateTimeID(), weekID, sequence, dayID));
        int id = 0;

        while (rs.next()) id = rs.getInt("id");

        if (id == 0) {
            stmt.executeUpdate(String.format(qrs.qAddDateTime(), weekID, sequence, dayID));
            stmt.close();
            id = getLastID("lessons_datetimes");
        }

        return id;
    }

    /**
     * Adds subject to respective table, if not exists.
     * @param info information about subject (displayble, like room, teacher, lesson activity, name).
     * @return id of the added/found subject.
     * @throws SQLException
     */
    @Override
    public int putSubject(String info) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qGetSubjectID(), info));
        int id = 0;

        while (rs.next()) id = rs.getInt("id");

        if (id == 0) {
            stmt.executeUpdate(String.format(qrs.qAddSubject(), info));
            stmt.close();

            id = getLastID("subjects");
        }


        return id;
    }

    /**
     * Adds the lesson record - which group has this lesson, at what datetime, and, obviously, what is the lesson
     * is on for this group at this particular datetime (week parity, day and time)
     * @param groupID the id of the group (should be taken from <code>groups</code> table.
     * @param dateTimeID the id of datetime (should be taken from <code>lessons_datetimes</code> table.
     * @param subjectID the id of the subject ((should be taken from <code>subjects</code> table.
     * @throws SQLException
     */
    @Override
    public void putLessonRecord(int groupID, int dateTimeID, int subjectID) throws SQLException {
        Statement stmt = conn.createStatement();
        if (!lessonExists(groupID, dateTimeID, subjectID))
            stmt.executeUpdate(String.format(qrs.qAddLessonRecord(), groupID, dateTimeID, subjectID));

        stmt.close();
    }

    /**
     * Gets list of departments, sorted by their printed names with parameters. Since 1.1 we
     * use list of parameters to make department entries more extensionable.
     * @return Map<String, Map</String, String/>> where key is department tag and Map<String, String> all the data with
     * provided names of positions.
     * @throws SQLException
     */
    @Override
    public Map<String, Map<String, String>> getDepartments() throws SQLException {
        Map<String, Map<String, String>> result = new LinkedHashMap<>();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(qrs.qGetDepartments());

        while (rs.next()) {
            Map<String, String> data = new HashMap<>();
            String tag = rs.getString("tag");
            data.put("name",rs.getString("name"));
            result.put(tag, data);

        }

        stmt.close();
        return result;
    }

    /**
     * Gets only tags from department table from DB.
     * @return List of Strings.
     * @throws SQLException
     */
    @Override
    public List<String> getDepartmentTags() throws SQLException {
        List<String> result = new ArrayList<>();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(qrs.qGetDepartmentTags());

        while (rs.next())
            result.add(rs.getString("tag"));

        stmt.close();
        return result;
    }

    /**
     * Get group names from groups table, based on department tag ('knt', 'ff' or so)
     * @param departmentTag department tag.
     * @return List of strings (some groups has non-numerical names @see org.ssutt.core.fetch.SSUDataFetcher)
     * @throws SQLException
     * @throws NoSuchDepartmentException
     */
    @Override
    public List<String> getGroups(String departmentTag) throws SQLException, NoSuchDepartmentException {
        if (departmentExists(departmentTag)) {
            List<String> result = new ArrayList<>();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(String.format(qrs.qGetGroups(), departmentTag));

            while (rs.next())
                result.add(rs.getString("name"));
            stmt.close();
            return result;
        } else throw new NoSuchDepartmentException();
    }

    /**
     * Gets group global id from <code>groups</code> table. Actually, converts its name and its department tag to
     * simple and fast number - id.
     * @param departmentTag the tag of the department, where the groups exists.
     * @param groupName its printed name.
     * @return the global id.
     * @throws SQLException
     * @throws NoSuchDepartmentException
     * @throws NoSuchGroupException
     */
    @Override
    public int getGroupID(String departmentTag, String groupName) throws SQLException,
            NoSuchDepartmentException, NoSuchGroupException {
        if (departmentExists(departmentTag)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(String.format(qrs.qGetGroupID(), departmentTag, groupName));
            int id = 0;
            while (rs.next()) id = rs.getInt("id");
            stmt.close();

            if (id == 0) throw new NoSuchGroupException();

            return id;
        } else throw new NoSuchDepartmentException();
    }

    /**
     * Gets displayable name from groups department (represented by tag) and its ID from <code>groups</code> table.
     * Actually, convert, opposite to getGroupID.
     * @param departmentTag the tag of the department, where the groups exists.
     * @param groupID its global ID.
     * @return the printed name.
     * @throws SQLException
     * @throws NoSuchDepartmentException
     * @throws NoSuchGroupException
     */
    @Override
    public String getGroupName(String departmentTag, int groupID) throws SQLException,
            NoSuchDepartmentException, NoSuchGroupException {
        if (departmentExists(departmentTag)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(String.format(qrs.qGetGroupName(), departmentTag, groupID));
            String name = "";
            while (rs.next()) name = rs.getString("name");
            stmt.close();

            if (name.length()==0) throw new NoSuchGroupException();

            return name;
        }
        else throw new NoSuchDepartmentException();
    }

    /**
     * Gets the whole timetable of the group, based on its id from <code>groups</code> table.
     * @param groupID the  global id of the group.
     * @return List\<String[]\> formatted line by line in ascending day and ascending sequence (of classes) way. Each
     * String[] contains week state, the name of the day, sequence (as String!) and information about subject.
     * @throws SQLException
     * @throws NoSuchGroupException
     * @throws EmptyTableException
     */
    @Override
    public List<String[]> getTT(int groupID) throws SQLException, NoSuchGroupException, EmptyTableException {
        if (groupExistsAsID(groupID)) {
            List<String[]> table = new ArrayList<>();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(String.format(qrs.qGetTT(),groupID));
            while (rs.next()) {
                String[] element = new String[4];
                element[0]=rs.getString("state");
                element[1]=rs.getString("name");
                element[2]=String.valueOf(rs.getInt("sequence"));
                element[3]=rs.getString("info");
                table.add(element);
            }
            if (table.size()==0) throw new EmptyTableException();

            return table;
        }
        else throw new NoSuchGroupException();
    }

    /**
     * Utility. Checks, if the department found by its tag.
     * @param departmentTag the tag of the department: knt, ff, sf or so.
     * @return <code>true</code> if found, else <code>false</code>.
     * @throws SQLException
     */
    @Override
    public boolean departmentExists(String departmentTag) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qDepartmentExists(), departmentTag));
        int id = 0;
        while (rs.next()) id = rs.getInt("id");

        stmt.close();
        return id != 0;
    }

    /**
     * Utility. Checks, if the specified department has this group.
     * @param departmentTag the tag of the department: knt, ff, sf or so.
     * @param groupName the displayable name to check.
     * @return <code>true</code> if found, else <code>false</code>.
     * @throws SQLException
     */
    @Override
    public boolean groupExistsInDepartment(String departmentTag, String groupName) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qGetGroupID(), departmentTag, groupName));
        int id = 0;
        while (rs.next()) id = rs.getInt("id");
        stmt.close();
        return (id != 0);
    }

    @Override
    public boolean groupExistsAsID(int groupID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qGroupIDExists(), groupID));
        int id = 0;
        while (rs.next()) id = rs.getInt("id");
        stmt.close();
        return (id != 0);
    }

    /**
     * Utility. Checks if the group has this subject at the specified datetime.
     * @param groupID the global id of the group.
     * @param dateTimeID datetime id to check.
     * @param subjectID the id of the subject (should be taken from <code>subjects</code> table).
     * @return <code>true</code> if group has this class at this particular time, else <code>false</code>
     * @throws SQLException
     */
    @Override
    public boolean lessonExists(int groupID, int dateTimeID, int subjectID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qSubjectExists(), groupID, dateTimeID, subjectID));
        int id = 0;

        while (rs.next()) id = rs.getInt("group_id");

        return id != 0;
    }

    /**
     * Utility. Gets the last ID of the specified table.
     * @param table checked table.
     * @return last ID.
     * @throws SQLException
     */
    @Override
    public int getLastID(String table) throws SQLException {
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(String.format(qrs.getLastID(), table));
        int id = 0;
        while (rs.next())
            id = rs.getInt("MAX(id)");
        return id;
    }

    /**
     * Initialization utility. Gets Queries instance to provide SQL queries definition for exact database
     * (H2DB, MySQL or so).
     * @param qrs initialized Queries implementation.
     */
    @Override
    public void setQueries(Queries qrs) {
        SSUSQLManager.qrs = qrs;
    }
}



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

import org.tt.core.fetch.entity.Department;
import org.tt.core.fetch.entity.Group;
import org.tt.core.sql.ex.NoSuchDepartmentException;
import org.tt.core.sql.ex.NoSuchGroupException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SSUSQLManager implements AbstractSQLManager {
    private static Connection conn;
    private static AbstractQueries qrs;

    public SSUSQLManager(Connection conn, AbstractQueries qrs) {
        SSUSQLManager.conn = conn;
        SSUSQLManager.qrs = qrs;
    }

    public SSUSQLManager(Connection conn) {
        SSUSQLManager.conn = conn;
    }

    @Override
    public void putDepartments(List<Department> departments) throws SQLException {
        Statement stmt = conn.createStatement();

        for (Department dep : departments) {
            if (!departmentExists(dep.getTag())) {
                stmt.executeUpdate(String.format(qrs.qAddDepartment(), dep.getName(), dep.getTag(), dep.getMessage()));
            }
        }

        stmt.close();
    }

    @Override
    public void putGroups(List<Group> groups, String departmentTag) throws SQLException, NoSuchDepartmentException {
        if (departmentExists(departmentTag)) {
            Statement stmt = conn.createStatement();

            for (Group g : groups) {
                if (!groupExistsInDepartment(departmentTag, g.getName())) {
                    stmt.executeUpdate(String.format(qrs.qAddGroups(), departmentTag, g.getName()));
                }
            }

            stmt.close();
        } else throw new NoSuchDepartmentException();
    }

    @Override
    public int putDateTime(int weekID, int sequence, int dayID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qGetDateTimeID(), weekID, sequence, dayID));
        int id = 0;

        while (rs.next()) id = rs.getInt("id");

        if (id == 0) {
            stmt.executeUpdate(String.format(qrs.qAddDateTime(), weekID, sequence, dayID));
            stmt.close();
            id = getLastID("datetimes");
        }

        return id;
    }

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

    @Override
    public int putLocation(String building, String room) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qGetLocationID(), building, room));
        int id = 0;

        while (rs.next()) id = rs.getInt("id");

        if (id == 0) {
            stmt.executeUpdate(String.format(qrs.qAddLocation(), building, room));
            stmt.close();

            id = getLastID("locations");
        }

        return id;

    }

    @Override
    public int putTeacher(String name) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qGetTeacherID(), name));
        int id = 0;

        while (rs.next()) id = rs.getInt("id");

        if (id == 0) {
            stmt.executeUpdate(String.format(qrs.qAddTeacher(), name));
            stmt.close();

            id = getLastID("teachers");
        }

        return id;

    }

    @Override
    public int putSubGroup(int groupID, String name) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qGetSubGroupID(), groupID, name));
        int id = 0;

        while (rs.next()) id = rs.getInt("id");

        if (id == 0) {
            stmt.executeUpdate(String.format(qrs.qAddSubGroup(), groupID, name));
            stmt.close();

            id = getLastID("subgroups");
        }

        return id;
    }

    @Override
    public void putLessonRecord(int groupID, int dateTimeID, int activityID, int subjectID, int subGroupID, int teacherID,
                                int locationID, long timestamp) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(String.format(qrs.qAddLessonRecord(), groupID, dateTimeID, activityID, subjectID, subGroupID, teacherID,
                locationID, timestamp));

        stmt.close();
    }

    @Override
    public List<Department> getDepartments() throws SQLException {
        List<Department> result = new ArrayList<>();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(qrs.qGetDepartments());

        while (rs.next()) {
            Department d = new Department();
            d.setName(rs.getString("name"));
            d.setTag(rs.getString("tag"));
            d.setMessage(rs.getString("message"));
            result.add(d);
        }

        stmt.close();
        return result;
    }

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

    @Override
    public List<Group> getGroups(String departmentTag) throws SQLException, NoSuchDepartmentException {
        if (departmentExists(departmentTag)) {
            List<Group> result = new ArrayList<>();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(String.format(qrs.qGetGroups(), departmentTag));

            while (rs.next()) {
                Group g = new Group(rs.getString("name"));
                result.add(g);
            }
            stmt.close();
            return result;
        } else throw new NoSuchDepartmentException();
    }

    @Override
    public List<Group> getNonEmptyGroups(String departmentTag) throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
        if (departmentExists(departmentTag)) {
            List<Group> result = new ArrayList<>();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(String.format(qrs.qGetGroups(), departmentTag));

            while (rs.next()) {
                Group g = new Group(rs.getString("name"));
                if (groupHasTable(getGroupID(departmentTag, g.getName())))
                    result.add(g);
            }
            stmt.close();
            return result;
        } else throw new NoSuchDepartmentException();
    }

    @Override
    public int getGroupID(String departmentTag, String groupName) throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
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

    @Override
    public String getGroupName(String departmentTag, int groupID) throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
        if (departmentExists(departmentTag)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(String.format(qrs.qGetGroupName(), departmentTag, groupID));
            String name = "";
            while (rs.next()) name = rs.getString("name");
            stmt.close();

            if (name.length() == 0) throw new NoSuchGroupException();

            return name;
        } else throw new NoSuchDepartmentException();
    }

    @Override
    public TTEntity getTT(int groupID) throws SQLException, NoSuchGroupException {
        if (groupExistsAsID(groupID)) {
            TTEntity result = new TTEntity();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(String.format(qrs.qGetTT(), groupID));

            while (rs.next()) {

                result.append(rs.getString("dayName"),
                        rs.getString("parity"),
                        rs.getInt("seq"),
                        rs.getString("activity"),
                        rs.getString("subject"),
                        rs.getString("sub"),
                        rs.getString("teacher"),
                        rs.getString("locb"),
                        rs.getString("locr")
                );

            }
            for (TTDayEntity ttde: result.getTimetable()) {
                System.out.println(ttde.getName());
                for (TTLesson ttl: ttde.getLessons()) {
                        for (TTLesson.TTLessonRecord ttlrd: ttl.getRecords()) {
                            System.out.print(
                                    ttl.getParity() + " " +
                                            ttl.getSequence() + " " + ttlrd.getActivity() + " " + ttlrd.getSubject()
                            );
                            for (TTLesson.TTClassRoomEntity ttcre: ttlrd.getClassRoomEntities()) {
                                System.out.print(" " + ttcre.getTeacher()+ " "+ttcre.getSubgroup()+" "+ttcre.getBuilding());
                            }
                            System.out.println();
                        }
                    System.out.println();
                    }
                System.out.println();
                }


        } else throw new NoSuchGroupException();
        return null;
    }

    @Override
    public boolean departmentExists(String departmentTag) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qDepartmentExists(), departmentTag));
        int id = 0;
        while (rs.next()) id = rs.getInt("id");

        stmt.close();
        return id != 0;
    }

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

    @Override
    public boolean lessonExists(int groupID, int dateTimeID, int subjectID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qSubjectExists(), groupID, dateTimeID, subjectID));
        int id = 0;

        while (rs.next()) id = rs.getInt("group_id");

        return id != 0;
    }

    @Override
    public boolean groupHasTable(int groupID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qGroupTTExists(), groupID));
        int num = 0;
        while (rs.next()) num = rs.getInt("result");

        return num != 0;
    }

    @Override
    public int getLastID(String table) throws SQLException {
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(String.format(qrs.getLastID(), table));
        int id = 0;
        while (rs.next())
            id = rs.getInt("MAX(id)");
        return id;
    }

    @Override
    public int getParityID(String state) throws SQLException {
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(String.format(qrs.qGetParityID(), state));

        int id = 0;

        while (rs.next())
            id = rs.getInt("id");

        return id;
    }

    @Override
    public int getActivityID(String type) throws SQLException {
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(String.format(qrs.qGetActivityID(), type));

        int id = 0;

        while (rs.next())
            id = rs.getInt("id");

        return id;
    }

    @Override
    public void setQueries(AbstractQueries qrs) {
        SSUSQLManager.qrs = qrs;
    }
}

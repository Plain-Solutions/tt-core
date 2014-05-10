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

import org.tt.core.entity.datafetcher.Department;
import org.tt.core.entity.datafetcher.Group;
import org.tt.core.entity.datafetcher.Lesson;
import org.tt.core.entity.db.TTEntity;
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
    public void putDepartment(Department department) throws SQLException {
        Statement stmt = conn.createStatement();
        if (!departmentExists(department.getTag())) {
            stmt.executeUpdate(String.format(qrs.qAddDepartment(), department.getName(), department.getTag(),
                    department.getMessage()));
        }
        stmt.close();
    }

    @Override
    public void putDepartments(List<Department> departments) throws SQLException {
        for (Department dep : departments) {
            putDepartment(dep);
        }
    }

    @Override
    public void putGroups(List<Group> groups, String departmentTag) throws SQLException, NoSuchDepartmentException {
            for (Group g : groups) {
                putGroup(g, departmentTag);
            }
    }

    @Override
    public void putGroup(Group group, String departmentTag) throws SQLException, NoSuchDepartmentException {
        if (departmentExists(departmentTag)) {
            Statement stmt = conn.createStatement();

            if (!groupExistsInDepartment(departmentTag, group.getName())) {
                stmt.executeUpdate(String.format(qrs.qAddGroups(), departmentTag, group.getName()));
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
    public void updateDepartmentMessage(String departmentTag, String newMessage) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(String.format(qrs.qUpdateDepartmentMessage(), newMessage, departmentTag));

        stmt.close();
    }

    @Override
    public void updateDepartmentInfo(String departmentName, String departmentTag, String departmentMessage, String originalTag) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(String.format(String.format(qrs.qUpdateDepartmentData(),
                departmentName, departmentTag, departmentMessage, originalTag)));

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
            d.setMessage("");
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
    public String getDepartmentMessage(String departmentTag) throws SQLException, NoSuchDepartmentException {
        String result = "";
        if (departmentExists(departmentTag)) {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qGetDepartmentMessage(), departmentTag));

        while(rs.next()) {
            result = rs.getString("message");
        }

        return result;
        } else throw new NoSuchDepartmentException();
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

            return result;
        } else throw new NoSuchGroupException();

    }

    @Override
    public List<List<Lesson>> getLessonList(int groupID) throws SQLException, NoSuchGroupException {
        if (groupExistsAsID(groupID)) {
            List<List<Lesson>> result = new ArrayList<>(6);

            Statement stmt = conn.createStatement();

            for (int i = 0; i < 6 ;i++) {
                List<Lesson> day = new ArrayList<>();

                ResultSet rs = stmt.executeQuery((String.format(qrs.qGetLessonList(), i+1, groupID)));
                while (rs.next()) {
                    Lesson l = new Lesson();
                    l.setSequence(rs.getInt("sequence"));
                    l.setParity(rs.getString("parity"));
                    l.setSubgroup(rs.getString("subgroup"));
                    l.setActivity(rs.getString("activity"));
                    l.setSubject(rs.getString("subject"));
                    l.setTeacher(rs.getString("teacher"));
                    l.setBuilding(rs.getString("building"));
                    l.setRoom(rs.getString("room"));
                    l.setTimestamp(rs.getLong("timestamp"));
                    day.add(l);
                }

                result.add(day);
            }
            return result;
        } else throw  new NoSuchGroupException();
    }

    @Override
    public void deleteDepartment(Department department) throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
        List<Group> groups = getGroups(department.getTag());

        for (Group g: groups) {
            deleteGroupFromDepartment(department, g);
        }

        Statement stmt = conn.createStatement();

        stmt.executeUpdate(String.format(qrs.qDeleteDepartment(), department.getTag()));

        stmt.close();
    }

    @Override
    public void deleteGroupFromDepartment(Department department, Group group) throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
        Statement stmt = conn.createStatement();

        int id = getGroupID(department.getTag(), group.getName());

        deleteLessons(department, group);
        deleteSubGroups(department, group);

        stmt.executeUpdate(String.format(qrs.qDeleteGroup(), id));

        stmt.close();
    }

    @Override
    public void deleteLessons(Department department, Group group) throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
        Statement stmt = conn.createStatement();

        int id = getGroupID(department.getTag(), group.getName());

        stmt.executeUpdate(String.format(qrs.qDeleteGroupLessons(), id));

        stmt.close();
    }

    @Override
    public void deleteSubGroups(Department department, Group group) throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
        Statement stmt = conn.createStatement();

        int id = getGroupID(department.getTag(), group.getName());

        stmt.executeUpdate(String.format(qrs.qDeleteGroupSubgroups(), id));

        stmt.close();
    }

    @Override
    public void deleteLesson(int groupID, int dateTimeID, int activityID, int subjectID, int subGroupID, int teacherID,
                             int locationID) throws SQLException {
        Statement stmt = conn.createStatement();

        stmt.executeUpdate(String.format(qrs.qDeleteLessonEntry(), groupID, dateTimeID, activityID, subjectID,
                subGroupID, teacherID, locationID));

    }

    @Override
    public void flushDatabase() throws SQLException {
        Statement stmt = conn.createStatement();
        String[] tables = {"lessons", "subgroups", "subjects", "teachers", "locations", "datetimes", "groups",
                "departments"};
        for (String t: tables) {
            stmt.executeUpdate(String.format(qrs.qGlobalDelete(), t));
            if (!t.equals("lessons"))
                stmt.executeUpdate(String.format(qrs.qIDRestart(), t));
        }

        stmt.close();
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

    private boolean departmentExists(String departmentTag) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qDepartmentExists(), departmentTag));
        int id = 0;
        while (rs.next()) id = rs.getInt("id");

        stmt.close();
        return id != 0;
    }


    private boolean groupExistsInDepartment(String departmentTag, String groupName) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qGetGroupID(), departmentTag, groupName));
        int id = 0;
        while (rs.next()) id = rs.getInt("id");
        stmt.close();
        return (id != 0);
    }


    private boolean groupExistsAsID(int groupID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qGroupIDExists(), groupID));
        int id = 0;
        while (rs.next()) id = rs.getInt("id");
        stmt.close();
        return (id != 0);
    }


    private boolean lessonExists(int groupID, int dateTimeID, int subjectID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qSubjectExists(), groupID, dateTimeID, subjectID));
        int id = 0;

        while (rs.next()) id = rs.getInt("group_id");

        return id != 0;
    }


    private boolean groupHasTable(int groupID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(qrs.qGroupTTExists(), groupID));
        int num = 0;
        while (rs.next()) num = rs.getInt("result");

        return num != 0;
    }

    private int getLastID(String table) throws SQLException {
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(String.format(qrs.getLastID(), table));
        int id = 0;
        while (rs.next())
            id = rs.getInt("MAX(id)");
        return id;
    }
}

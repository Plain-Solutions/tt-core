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

/**
 * H2Queries is an implementation of AbstractQueries interface to help AbstractSQLManager to communicate with H2 database
 * <p/>
 * All the query definitions in this class are created specially for H2. Compatibility with other database providers
 * is not guaranteed.
 *
 * @author Vlad Slepukhin
 * @since 1.0
 */
public class H2Queries implements AbstractQueries {

    public H2Queries() {
    }

    /**
     * Query description. Gets the last id in the given table.
     * <p/>
     * The main usage is it get the last added class or datetime and pass it back to
     * lessons_records
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String getLastID() {
        return "SELECT MAX(id) FROM %s";
    }

    /**
     * Query description. Adds department to <code>departments</code> table
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String qAddDepartment() {
        return "INSERT INTO departments(name,tag, message) VALUES('%s','%s', '%s');";
    }

    /**
     * Query description. Adds group to <code>groups</code> table, based on department_tag information
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String qAddGroups() {
        return "INSERT INTO GROUPS(department_id, name) VALUES" +
                "((SELECT id FROM DEPARTMENTS WHERE tag='%s'),'%s'); ";
    }

    /**
     * Query description. Adds datetime entry to <code>datetimes</code> table to handle all the variations
     * of lesson parity, sequence of them and day order
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String qAddDateTime() {
        return "INSERT INTO datetimes(week_id, sequence, day_id) VALUES(%d,%d,%d);";
    }

    /**
     * Query description. Adds subject entry to <code>subjects</code> table to store them for further use.
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String qAddSubject() {
        return "INSERT INTO subjects(name) VALUES ('%s');";
    }

    @Override
    public String qAddTeacher() {
        return "INSERT INTO teachers(name) VALUES('%s');";
    }

    @Override
    public String qAddLocation() {
        return "INSERT INTO locations(building, room) VALUES('%s', '%s');";
    }

    @Override
    public String qAddSubGroup() {
        return "INSERT INTO subgroups(group_id, name) VALUES(%d, '%s');";
    }

    /**
     * Query description. Adds lesson entry to <code>lessons_records</code> table to interconnect group, subject and datetime entries
     * in respective tables for fetching structured information about group timetable
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String qAddLessonRecord() {
        return "INSERT INTO lessons  (group_id," +
                "  datetime_id," +
                "  activity_id," +
                "  subject_id," +
                "  subgroup_id," +
                "  teacher_id," +
                "  location_id," +
                "  timestamp) VALUES (%d, %d, %d, %d, %d, %d, %d, %d);";
    }

    /**
     * Query description. Gets map name-tag from <code>departments table</code>
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String qGetDepartments() {
        return "SELECT name,tag, message FROM departments ORDER BY NAME;";
    }

    /**
     * Query description. Exclusively gets tags of departments, because of their wide usage in the whole TT project
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String qGetDepartmentTags() {
        return "SELECT tag FROM departments;";
    }

    /**
     * Query description. Gets all the groups names (displayable) from <code>groups</code> table, based on the
     * department tag.
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String qGetGroups() {
        return "SELECT gr.name FROM groups as gr, departments as dp " +
                "WHERE gr.department_id = dp.id AND dp.tag = '%s';";
    }

    /**
     * Query description.Gets group id from <code>groups table</code>, based on its name and department tag.
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String qGetGroupID() {
        return "SELECT gr.id FROM groups as gr, departments as dp " +
                "WHERE gr.department_id = dp.id AND dp.tag = '%s' AND gr.name = '%s'";
    }

    /**
     * Query description. Gets group name from <code>groups</code> table, based on its id and department tag.
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String qGetGroupName() {
        return "SELECT gr.name FROM groups as gr, departments as dp " +
                "WHERE gr.department_id = dp.id AND dp.tag = '%s' AND gr.id = %d";
    }

    /**
     * Query description. Gets id of the datetime record from <code>datetimes</code>.
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String qGetDateTimeID() {
        return "SELECT id FROM datetimes WHERE week_id=%d AND sequence=%d AND day_id=%d;";
    }

    /**
     * Query description. Gets id of the subject from <code>subjects</code> table.
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String qGetSubjectID() {
        return "SELECT id FROM subjects WHERE name='%s';";
    }

    /**
     * Query description. Gets id of the teacher from <code>teachers</code> table.
     *
     * @return <code>String</code> containing SQL query.
     * @since 2.0
     */
    @Override
    public String qGetTeacherID() {
        return "SELECT id FROM teachers WHERE name='%s';";
    }

    /**
     * Query description. Gets id of the location (building + room) from <code>locations</code> table.
     *
     * @return <code>String</code> containing SQL query.
     * @since 2.0
     */
    @Override
    public String qGetLocationID() {
        return "SELECT id FROM locations WHERE building='%s' AND room='%s';";
    }

    /**
     * Query description. Gets id of the subgroup from <code>subgroups</code> table.
     *
     * @return <code>String</code> containing SQL query.
     * @since 2.0
     */
    @Override
    public String qGetSubGroupID() {
        return "SELECT id FROM subgroups WHERE  group_id=%d AND name='%s';";
    }

    /**
     * Query description. Gets id of parity state from <code>week_states</code> table.
     *
     * @return <code>String</code> containing SQL query.
     * @since 2.0
     */
    @Override
    public String qGetParityID() {
        return "SELECT id FROM week_states WHERE state='%s';";
    }

    /**
     * Query description. Gets id of the activity type (lecture and so on) from <code>activities</code> table.
     *
     * @return <code>String</code> containing SQL query.
     * @since 2.0
     */
    @Override
    public String qGetActivityID() {
        return "SELECT id FROM activities WHERE type='%s';";
    }

    /**
     * Query description. Gets structured information about the whole timetable for the selected group
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String qGetTT() {
        return "SELECT d.name As dayName, ws.state As parity, " +
                "ldt.sequence As seq, a.type As activity, s.name As subject, " +
                "sgrp.name As sub, t.name As teacher, loc.building As locb, loc.room As locr " +
                "FROM week_states as ws " +
                "JOIN datetimes as ldt on ldt.week_id=ws.id " +
                "JOIN days as d on d.id=ldt.day_id " +
                "JOIN lessons as lr on lr.datetime_id = ldt.id " +
                "JOIN activities as a on lr.activity_id = a.id " +
                "JOIN subjects as s on lr.subject_id = s.id  " +
                "JOIN subgroups as sgrp on  lr.subgroup_id = sgrp.id " +
                "JOIN teachers as t on lr.teacher_id = t.id " +
                "JOIN locations as loc on lr.location_id = loc.id " +
                "JOIN groups as g on g.id = lr.group_id AND g.id=%d " +
                "ORDER BY d.id ASC, ldt.sequence ASC , sgrp.name ASC;";
    }

    @Override
    public String qGetLessonList() {
        return "SELECT  ldt.sequence As sequence, ws.state As parity,   sgrp.name As subgroup," +
                "a.type As activity, s.name As subject,  " +
                "t.name As teacher, loc.building As building, loc.room As room, lr.timestamp As timestamp " +
                "FROM week_states as ws  " +
                "JOIN datetimes as ldt on ldt.week_id=ws.id  " +
                "JOIN days as d on d.id=ldt.day_id AND d.id=%d  " +
                "JOIN lessons as lr on lr.datetime_id = ldt.id  " +
                "JOIN activities as a on lr.activity_id = a.id  " +
                "JOIN subjects as s on lr.subject_id = s.id   " +
                "JOIN subgroups as sgrp on  lr.subgroup_id = sgrp.id  " +
                "JOIN teachers as t on lr.teacher_id = t.id  " +
                "JOIN locations as loc on lr.location_id = loc.id  " +
                "JOIN groups as g on g.id = lr.group_id AND g.id=%d" +
                "ORDER BY d.id ASC, ldt.sequence ASC , sgrp.name ASC;";
    }

    @Override
    public String qDeleteDepartment() {
        return "DELETE FROM departments WHERE tag='%s';";
    }

    @Override
    public String qDeleteGroup() {
        return "DELETE FROM groups WHERE id=%d;";
    }

    @Override
    public String qDeleteGroupSubgroups() {
        return "DELETE FROM subgroups WHERE group_id=%d;";
    }

    @Override
    public String qDeleteGroupLessons() {
        return "DELETE FROM lessons WHERE group_id=%d;";
    }

    @Override
    public String qDeleteLessonEntry() {
        return "DELETE FROM lessons WHERE group_id=%s AND datetime_id=%d AND activity_id=%d AND subject_id=%d AND " +
                "subgroup_id = %d AND teacher_id=%d AND location_id=%d; ";
    }

    @Override
    public String qUpdateDepartmentMessage() {
        return "UPDATE departments SET message='%s' WHERE tag='%s';";
    }

    @Override
    public String qUpdateDepartmentData() {
        return "UPDATE departments SET name='%s', tag='%s', message='%s'  WHERE tag='%s';";
    }

    /**
     * Query description. Utility. Gets id of the department to proof its existence.
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String qDepartmentExists() {
        return "SELECT id FROM departments WHERE tag='%s'";
    }

    /**
     * Query description. Utility. Gets id of the group to proof its existence.
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String qGroupIDExists() {
        return "SELECT gr.id FROM groups as gr WHERE id=%d";
    }

    /**
     * Query description. Utility. Gets id of the subject to proof its existence.
     *
     * @return <code>String</code> containing SQL query.
     * @since 1.0
     */
    @Override
    public String qSubjectExists() {
        return "SELECT group_id FROM lessons WHERE group_id=%d AND datetime_id=%d AND subject_id=%d;";
    }

    /**
     * Query description. Utility. Checks if any lesson entries belong to selected group.
     *
     * @return <code>String</code> containing SQL query.
     * @since 2.0
     */
    @Override
    public String qGroupTTExists() {
        return "SELECT COUNT(group_id) As result FROM lessons WHERE group_id=%d;";
    }
}

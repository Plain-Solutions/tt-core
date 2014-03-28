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

/**
 * H2Queries is an implementation of Queries interface to help TTSQLManager to communicate with H2 database
 * <p>
 * All the query definitions in this class are created specially for H2. Compatibility with other database providers
 * is not guaranteed.
 *
 * @author Vlad Slepukhin
 * @since 1.0
 */
public class H2Queries implements Queries {

    public H2Queries() {
    }

    /**
     * Query description. Gets the last id in the given table.
     * <p>
     * The main usage is it get the last added class or datetime and pass it back to
     * lessons_records
     *
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String getLastID() {
        return "SELECT MAX(id) FROM %s";
    }

    /**
     * Query description. Adds department to <code>departments</code> table
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qAddDepartment() {
        return "INSERT INTO departments(name,tag) VALUES('%s','%s');";
    }

    /**
     * Query description. Adds group to <code>groups</code> table, based on department_tag information
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qAddGroups() {
        return "INSERT INTO GROUPS(department_id, name) VALUES" +
                "((SELECT id FROM DEPARTMENTS WHERE tag='%s'),'%s'); ";
    }

    /**
     * Query description. Adds datetime entry to <code>lessons_datetimes</code> table to handle all the variations
     * of lesson parity, sequence of them and day order
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qAddDateTime() {
        return "INSERT INTO lessons_datetimes(week_id, sequence, day_id) VALUES(%d,%d,%d);";
    }

    /**
     * Query description. Adds subject entry to <code>subjects</code> table to store them for further use.
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qAddSubject() {
        return "INSERT INTO subjects(info) VALUES ('%s');";
    }

    /**
     * Query description. Adds lesson entry to <code>lessons_records</code> table to interconnect group, subject and datetime entries
     * in respective tables for fetching structured information about group timetable
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qAddLessonRecord() {
        return "INSERT INTO lessons_records(group_id, datetime_id,subject_id) VALUES(%d,%d,%d);";
    }

    /**
     * Query description. Gets map name-tag from <code>departments table</code>
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qGetDepartments() {
        return "SELECT name,tag FROM departments;";
    }

    /**
     * Query description. Exclusively gets tags of departments, because of their wide usage in the whole TT project
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qGetDepartmentTags() {
        return "SELECT tag FROM departments;";
    }

    /**
     * Query description. Converts department tag to the id.
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qGetDepartmentTagByID() {
        return "SELECT tag FROM departments WHERE id=%d";
    }

    /**
     * Query description. Converts department id to the printed name
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qGetDepartmentNameByID() {
        return "SELECT name FROM departments WHERE id=%d";
    }

    /**
     * Query description. Gets tag of the department by the name.
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qGetDepartmentTagByName() {
        return "SELECT tag FROM departments WHERE name='%s'";
    }

    /**
     * Query description. Gets all the groups names (displayble) from <code>groups</code> table, based on the
     * department tag.
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qGetGroups() {
        return "SELECT gr.name FROM groups as gr, departments as dp " +
                "WHERE gr.department_id = dp.id AND dp.tag = '%s';";
    }

    /**
     * Query description.Gets group id from <code>groups table</code>, based on its name and department tag.
     * @return
     */
    @Override
    public String qGetGroupID() {
        return "SELECT gr.id FROM groups as gr, departments as dp " +
                "WHERE gr.department_id = dp.id AND dp.tag = '%s' AND gr.name = '%s'";
    }

    /**
     * Query description. Gets group name from <code>groups</code> table, based on its id and department tag.
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qGetGroupName() {
        return "SELECT gr.name FROM groups as gr, departments as dp " +
                "WHERE gr.department_id = dp.id AND dp.tag = '%s' AND gr.id = %d";
    }

    /**
     * Query description. Gets id of the datetime record from <code>lessons_datetimes</code>.
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qGetDateTimeID() {
        return "SELECT id FROM lessons_datetimes WHERE week_id=%d AND sequence=%d AND day_id=%d;";
    }

    /**
     * Query description. Gets id of the subject from <code>subjects</code> table.
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qGetSubjectID() {
        return "SELECT id FROM subjects WHERE info='%s';";
    }

    /**
     * Query description. Gets structured information about the whole timetable for the selected group
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qGetTT() {
        return "SELECT ws.state, d.name, ldt.sequence, s.info " +
                "FROM week_states as ws " +
                "JOIN lessons_datetimes as ldt on ldt.week_id=ws.id " +
                "JOIN days as d on d.id=ldt.day_id " +
                "JOIN lessons_records as lr on lr.datetime_id = ldt.id " +
                "JOIN subjects as s on s.id = lr.subject_id " +
                "JOIN groups as g on g.id = lr.group_id AND g.id=%d " +
                "ORDER BY d.id ASC, ldt.sequence ASC ";
    }

    /**
     * Query description. Utility. Gets id of the department to proof its existence.
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qDepartmentExists() {
        return "SELECT id FROM departments WHERE tag='%s'";
    }

    /**
     * Query description. Utility. Gets id of the group to proof its existence.
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qGroupIDExists() {
        return "SELECT gr.id FROM groups as gr WHERE id=%d";
    }

    /**
     * Query description. Utility. Gets id of the subject to proof its existence.
     * @return <code>String</code> containing SQL query.
     */
    @Override
    public String qSubjectExists() {
        return "SELECT group_id FROM lessons_records WHERE group_id=%d AND datetime_id=%d AND subject_id=%d;";
    }
}

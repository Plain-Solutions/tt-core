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

public class H2Queries implements Queries {

    public H2Queries() {
    }

    @Override
    public String getLastID() {
        return "SELECT MAX(id) FROM %s";
    }

    @Override
    public String qAddDepartment() {
        return "INSERT INTO departments(name,tag) VALUES('%s','%s');";
    }

    @Override
    public String qGetDepartments() {
        return "SELECT name,tag FROM departments;";
    }

    @Override
    public String qGetDepartmentTags() {
        return "SELECT tag FROM departments;";
    }

    @Override
    public String qGetDepartmentTagByID() {
        return "SELECT tag FROM departments WHERE id=%d";
    }

    @Override
    public String qGetDepartmentNameByID() {
        return "SELECT name FROM departments WHERE id=%d";
    }

    @Override
    public String qGetDepartmentTagByName() {
        return "SELECT tag FROM departments WHERE name='%s'";
    }

    @Override
    public String qDepartmentExists() {
        return "SELECT id FROM departments WHERE tag='%s'";
    }

    @Override
    public String qGroupIDExists() {
        return "SELECT gr.id FROM groups as gr WHERE id=%d";
    }

    @Override
    public String qAddGroups() {
        return "INSERT INTO GROUPS(department_id, name) VALUES" +
                "((SELECT id FROM DEPARTMENTS WHERE tag='%s'),'%s'); ";
    }

    @Override
    public String qGetGroups() {
        return "SELECT gr.name FROM groups as gr, departments as dp " +
                "WHERE gr.department_id = dp.id AND dp.tag = '%s';";
    }

    @Override
    public String qGetGroupID() {
        return "SELECT gr.id FROM groups as gr, departments as dp " +
                "WHERE gr.department_id = dp.id AND dp.tag = '%s' AND gr.name = '%s'";
    }

    @Override
    public String qGetGroupName() {
        return "SELECT gr.name FROM groups as gr, departments as dp " +
                "WHERE gr.department_id = dp.id AND dp.tag = '%s' AND gr.id = %d";
    }

    @Override
    public String qAddDateTime() {
        return "INSERT INTO lessons_datetimes(week_id, sequence, day_id) VALUES(%d,%d,%d);";
    }

    @Override
    public String qGetDateTimeID() {
        return "SELECT id FROM lessons_datetimes WHERE week_id=%d AND sequence=%d AND day_id=%d;";
    }

    @Override
    public String qAddSubject() {
        return "INSERT INTO subjects(info) VALUES ('%s');";
    }

    @Override
    public String qGetSubjectID() {
        return "SELECT id FROM subjects WHERE info='%s';";
    }

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

    @Override
    public String qAddLessonRecord() {
        //some strange bug during deployment and maven packaging on IDEA
        return "INSERT INTO lessons_records(group_id, datetime_id,subject_id) VALUES(%d,%d,%d);";
    }

    @Override
    public String qSubjectExists() {
        return "SELECT group_id FROM lessons_records WHERE group_id=%d AND datetime_id=%d AND subject_id=%d;";
    }


}

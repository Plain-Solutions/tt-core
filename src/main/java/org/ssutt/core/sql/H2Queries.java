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

    public H2Queries() {}

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
    public String qAddGroups() {
        return "INSERT INTO GROUPS(department_id, name) VALUES" +
                "((SELECT id FROM DEPARTMENTS WHERE tag='%s'),'%s'); ";
    }

    @Override
    public String qGetGroups() {
        return "SELECT gr.name FROM groups as gr, departments as dp " +
                "WHERE gr.department_id = dp.id AND dp.tag = '%s';";
    }
}

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
 * AbstractQueries is the interface that contains method descriptions for
 * all the needed operations in AbstractSQLManager. Simply, queries definitions.
 * <p/>
 * We use abstraction to allow developers change the provider of the database
 * and use, for instance MySQL instead of H2 or has
 * different implementation and realization of these queries.
 * <p/>
 * For more information please follow `see` link.
 *
 * @author Vlad Slepukhin
 * @see org.ssutt.core.sql.H2Queries
 * @since 1.0
 */
public interface AbstractQueries {

    String getLastID();

    String qAddDepartment();

    String qAddGroups();

    String qAddDateTime();

    String qAddSubject();

    String qAddLessonRecord();

    String qGetDepartments();

    String qGetDepartmentTags();

    String qGetDepartmentTagByID();

    String qGetDepartmentNameByID();

    String qGetDepartmentTagByName();


    String qGetGroups();

    String qGetGroupID();

    String qGetGroupName();

    String qGetDateTimeID();

    String qGetSubjectID();

    String qGetTT();

    String qDepartmentExists();

    String qGroupIDExists();

    String qSubjectExists();
}

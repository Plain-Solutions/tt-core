/*
 * Copyright 2014 Plain Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tt.core.dm;

import org.tt.core.fetch.entity.Department;
import org.tt.core.fetch.entity.Group;
import org.tt.core.sql.TTEntity;

import java.util.List;

/**
 * AbstractDataConverter is an interface, which allows to convert raw data to
 * some suitable representation, which can be casted to <code>java.lang.String</code>, like YAML or JSON.
 * <p/>
 * TT Core now has only serializers for JSON, using GSON. For example of implementation and execution results go
 * for JSONConverter.
 *
 * @author Vlad Slepukhin
 * @see org.tt.core.dm.convert.json.JSONConverter
 * @since 1.2
 */
public interface AbstractDataConverter {

    /**
     * Converts raw data in Java Object, containing information about all departments in university to
     * suitable representation.
     *
     * @param departments raw data.
     * @return String of some format.
     * @since 1.2
     */
    String convertDepartmentList(List<Department> departments);

    /**
     * Converts raw data in Java Object, containing information about all groups of the department to
     * suitable representation.
     *
     * @param names raw data.
     * @return String of some format.
     * @since 1.2
     */
    String convertGroupList(List<Group> names);

    /**
     * General-purposed converter of <code>List<String></code> to string view.
     * suitable representation.
     *
     * @param list raw data.
     * @return String of some format.
     * @since 1.2
     */
    String convertAbstractList(List<String> list);

    /**
     * Converts list of all classes in group to formatted output.
     *
     * @param table data from database sorted by day tags, sequence of lessons and parity (even<odd).
     * @return String of some format.
     * @since 2.0
     */
    String convertTT(TTEntity table);

    /**
     * Creates error report.
     *
     * @param module failing class or module.
     * @param msg    the detailed information.
     * @return Error report.
     * @since 1.2
     */
    String convertStatus(TTStatus module, String msg);

    /**
     * Creates error report.
     *
     * @param module failing class or module.
     * @param err    the detailed information as listed in {@link TTStatus}.
     * @return Error report.
     * @since 1.2
     */
    String convertStatus(TTStatus module, TTStatus err);
}

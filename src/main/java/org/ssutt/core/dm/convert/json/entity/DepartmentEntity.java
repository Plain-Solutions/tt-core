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

package org.ssutt.core.dm.convert.json.entity;

import java.util.Map;

/**
 * DepartmentEntity is a accessor class for {@link org.ssutt.core.dm.convert.json.JSONConverter} to
 * reformat Java Object to JsonObject properly.
 *
 * @author Vlad Slepukhin
 * @since 1.2
 */
public class DepartmentEntity {
    private String tag;
    private Map<String, String> data;

    /**
     * Creates entity.
     *
     * @param tag  the tag of the department.
     * @param data all the data about the department in key-value representation (name: 'smth' or so).
     */
    public DepartmentEntity(String tag, Map<String, String> data) {
        this.tag = tag;
        this.data = data;
    }

    /**
     * Accessor for {@link org.ssutt.core.dm.convert.json.serializer.DepartmentSerializer}.
     *
     * @return the tag of the department.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Accessor for {@link org.ssutt.core.dm.convert.json.serializer.DepartmentSerializer}.
     *
     * @return K-V representation of data for JSON.
     */
    public Map<String, String> getData() {
        return data;
    }
}


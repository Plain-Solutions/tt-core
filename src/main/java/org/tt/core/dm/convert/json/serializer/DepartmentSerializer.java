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

package org.tt.core.dm.convert.json.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.tt.core.dm.convert.json.entity.DepartmentEntity;
import org.tt.core.fetch.entity.Department;

import java.lang.reflect.Type;

/**
 * DepartmentSerializer is override of standard GSON  <code>JsonSerializer</code> to properly create the list of departments.
 * It is now formatted as. We need a list inside "bf" or "gf" or so to be able extend some data about departments:
 * mails, phones, locations.
 * <code>    <br>
 * {"bf": <br>
 * {"name":"Some department"}, <br>
 * "gf":{"name":"Another department"}} <br>
 * </code>
 *
 * @author Vlad Slepukhin
 * @since 1.2
 */
public class DepartmentSerializer implements JsonSerializer<Department> {

    /**
     * Converts DepartmentEntity to JsonElement, saving order in information in right representation.
     *
     * @param departmentEntity         the instance of {@link org.tt.core.dm.convert.json.entity.DepartmentEntity}.
     * @param type                     default GSON parameter.
     * @param jsonSerializationContext default GSON parameter.
     * @return Formatted JSON Element - sub-array with info about the department and its tag.
     * @since 1.2
     */
    @Override
    public JsonElement serialize(Department department, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        JsonObject props = new JsonObject();
        props.addProperty("name", department.getName());
        props.addProperty("msg", department.getMessage());

        result.add(department.getTag(), props);
        return result;
    }
}

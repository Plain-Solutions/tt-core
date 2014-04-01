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

package org.ssutt.core.dm.convert.json.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.ssutt.core.dm.convert.json.entity.DepartmentEntity;

import java.lang.reflect.Type;

public class DepartmentSerializer implements JsonSerializer<DepartmentEntity> {
    @Override
    public JsonElement serialize(DepartmentEntity departmentEntity, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        JsonObject props = new JsonObject();
        for (String data: departmentEntity.getData().keySet())
            props.addProperty(data, departmentEntity.getData().get(data));

        result.add(departmentEntity.getTag(),props);
        return result;
    }
}

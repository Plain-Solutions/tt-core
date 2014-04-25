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


import com.google.gson.*;
import org.tt.core.fetch.entity.Group;

import java.lang.reflect.Type;

/**
 * GroupSerializer is override of standard GSON  <code>JsonSerializer</code> to properly create the list of groups.
 * Actually, it is an override over List<String> serializer
 * <code>["111", "123", "234"]</code>
 *
 * @author Vlad Slepukhin
 * @since 2.0
 */
public class GroupListSerializer implements JsonSerializer<Group> {

    /**
     * Converts a Group element to JsonElement, saving order in information in right representation.
     *
     * @param group                    the instance of {@link org.tt.core.fetch.entity.Group}.
     * @param type                     default GSON parameter.
     * @param jsonSerializationContext default GSON parameter.
     * @return Formatted JSON Element - sub-array with info about the department and its tag.
     * @since 2.0
     */
    @Override
    public JsonElement serialize(Group group, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(group.getName());
    }
}

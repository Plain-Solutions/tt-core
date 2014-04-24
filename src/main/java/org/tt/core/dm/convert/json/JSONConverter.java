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
package org.tt.core.dm.convert.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.tt.core.dm.AbstractDataConverter;
import org.tt.core.dm.TTStatus;
import org.tt.core.dm.convert.json.serializer.DepartmentSerializer;
import org.tt.core.dm.convert.json.serializer.GroupListSerializer;
import org.tt.core.dm.convert.json.serializer.TimeTableSerializer;
import org.tt.core.fetch.entity.Department;
import org.tt.core.fetch.entity.Group;
import org.tt.core.sql.TTEntity;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * JSONConverter is an implementation of {@link org.tt.core.dm.AbstractDataConverter}, which formats raw data,
 * represented by Java Objects, mainly Collections to JSON Strings via GSON with help of some entity for manual
 * conversion and serializers for them.
 *
 * @author Vlad Slepukhin
 * @since 1.2
 */
public class JSONConverter implements AbstractDataConverter {
    private static Gson gson;

    /**
     * Constructor creates a Gson instance to convert objects all over the class with a single
     * class instance.
     */
    public JSONConverter() {
        gson = new Gson();
    }

    /**
     * Converts <code>getDepartments</code> of {@link org.tt.core.sql.AbstractSQLManager} to JSON String.
     *
     * @param departments list of departments represented in Java Object entity.
     * @return JSON-formatted representation of raw object. See {@link org.tt.core.dm.convert.json.serializer.DepartmentSerializer}
     * or API reference for the output format.
     */
    public String convertDepartmentList(List<Department> departments) {
        GsonBuilder gsb = new GsonBuilder();
        gsb.registerTypeAdapter(Department.class, new DepartmentSerializer());

        return gsb.create().toJson(departments);
    }

    /**
     * Converts <code>getGroupNames</code> of {@link org.tt.core.sql.AbstractSQLManager} to JSON String.
     *
     * @param names list of departments represented in Java Object entity.
     * @return JSON-formatted representation of raw object. <code>List<String></code> to <code>[""]</code>
     */
    public String convertGroupList(List<Group> names) {
        GsonBuilder gsb = new GsonBuilder();
        gsb.registerTypeAdapter(Group.class, new GroupListSerializer());

        return gsb.create().toJson(names);

    }

    /**
     * General-purpose conversion for inner usage mainly.
     *
     * @param list some generic <code>java.lang.List<String></code>
     * @return [""] representation of List.
     */
    @Override
    public String convertAbstractList(List<String> list) {
        return gson.toJson(list);
    }

    /**
     * Inner-used conversion <code>getGroupID</code> of {@link org.tt.core.dm.AbstractDataManager} for JSON format. Actually int
     * to String conversion due to encapsulation and the unified TTData interface packed as String.
     *
     * @param id the numerical representation.
     * @return String representation.
     */
    public String convertGroupName(int id) {
        return gson.toJson(id);
    }

    /**
     * Converts <code>getTT</code> of {@link org.tt.core.sql.AbstractSQLManager} to JSON String.
     *
     * @param table list of classes, sorted ascending by days, times of classes and parity.
     * @return JSON-formatted representation of raw object. See {@link org.tt.core.dm.convert.json.serializer.TimeTableSerializer}
     * or API reference for the output format.
     */
    public String convertTT(TTEntity table) {
        GsonBuilder gsb = new GsonBuilder();



        gsb.registerTypeAdapter(TTEntity.class, new TimeTableSerializer());
        return gsb.create().toJson(table);
    }

    /**
     * Converts status information for JSON output in TT Platform.
     *
     * @param module element of {@link org.tt.core.dm.TTStatus} enum.
     * @param msg    the message about error/success.
     * @return JSON representation. See {@link StatusEntity}.
     */
    @Override
    public String convertStatus(TTStatus module, String msg) {
        return gson.toJson(new StatusEntity(module.toString(), msg));
    }

    /**
     * Converts status information for JSON output in TT Platform.
     *
     * @param module element of {@link org.tt.core.dm.TTStatus} enum.
     * @param state    element of {@link org.tt.core.dm.TTStatus} enum.
     * @return JSON representation. See {@link StatusEntity}.
     */
    @Override
    public String convertStatus(TTStatus module, TTStatus state) {
        return gson.toJson(new StatusEntity(module.toString(), state.message(state)));
    }

    /**
     * General-purposed converter string back to <code>List<String></code>  suitable representation.
     *
     * @param list of some format.
     * @return raw data.
     */
    @Override
    public List<String> reverseConvertGroup(String list) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(list, listType);
    }

    /**
     * General purposed converting from K-V structure to JSON
     * @param data map
     * @return resulting data
     * @since 1.2
     */
    @Override
    public String convertMap(Map<String, String> data) {
        return gson.toJson(data);
    }

}


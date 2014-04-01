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
package org.ssutt.core.dm.convert.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ssutt.core.dm.AbstractDataConverter;
import org.ssutt.core.dm.TTModule;
import org.ssutt.core.dm.convert.json.entity.DepartmentEntity;
import org.ssutt.core.dm.convert.json.entity.FailureEntity;
import org.ssutt.core.dm.convert.json.entity.TimeTableEntity;
import org.ssutt.core.dm.convert.json.serializer.DepartmentSerializer;
import org.ssutt.core.dm.convert.json.serializer.TimeTableSerializer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JSONConverter is an implementation of {@link org.ssutt.core.dm.AbstractDataConverter}, which formats raw data,
 * represented by Java Objects, mainly Collections to JSON Strings via GSON with help of some entities for manual
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
     * Converts <code>getDepartments</code> of {@link org.ssutt.core.sql.AbstractSQLManager} to JSON String.
     *
     * @param departments list of departments represented in Java Object entity.
     * @return JSON-formatted representation of raw object. See {@link org.ssutt.core.dm.convert.json.serializer.DepartmentSerializer}
     * or API reference for the output format.
     */
    public String convertDepartmentList(Map<String, Map<String, String>> departments) {
        GsonBuilder gsb = new GsonBuilder();
        gsb.registerTypeAdapter(DepartmentEntity.class, new DepartmentSerializer());

        return gsb.create().toJson(departments);
    }

    /**
     * Converts <code>getGroupNames</code> of {@link org.ssutt.core.sql.AbstractSQLManager} to JSON String.
     *
     * @param names list of departments represented in Java Object entity.
     * @return JSON-formatted representation of raw object. <code>List<String></code> to <code>[""]</code>
     */
    public String convertGroupList(List<String> names) {
        return convertAbstractList(names);
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
     * Inner-used conversion <code>getGroupID</code> of {@link org.ssutt.core.dm.AbstractDataManager} for JSON format. Actually int
     * to String conversion due to encapsulation and the unified TTData interface packed as String.
     *
     * @param id the numerical representation.
     * @return String representation.
     */
    public String convertGroupName(int id) {
        return gson.toJson(id);
    }

    /**
     * Converts <code>getTT</code> of {@link org.ssutt.core.sql.AbstractSQLManager} to JSON String.
     *
     * @param table list of classes, sorted ascending by days, times of classes and parity.
     * @return JSON-formatted representation of raw object. See {@link org.ssutt.core.dm.convert.json.serializer.TimeTableSerializer}
     * or API reference for the output format.
     */
    public String convertTT(List<String[]> table) {
        GsonBuilder gsb = new GsonBuilder();

        Map<String, List<Map<String, String>>> temp = new LinkedHashMap<>();

        for (String[] record : table) {
            String weekday = record[0];
            Map<String, String> t = new LinkedHashMap<>();
            t.put("parity", record[1]);
            t.put("sequence", record[2]);
            t.put("info", record[3]);
            if (temp.containsKey(weekday)) {
                temp.get(weekday).add(t);
            } else {
                List<Map<String, String>> tT = new ArrayList<>();
                tT.add(t);
                temp.put(weekday, tT);
            }

        }

        gsb.registerTypeAdapter(TimeTableEntity.class, new TimeTableSerializer());
        return gsb.create().toJson(temp);
    }

    /**
     * Converts failures information for JSON output in TT Platform.
     *
     * @param module element of {@link org.ssutt.core.dm.TTModule} enum.
     * @param msg    the message about error.
     * @return JSON representation. See {@link org.ssutt.core.dm.convert.json.entity.FailureEntity}.
     */
    @Override
    public String convertFailure(TTModule module, String msg) {
        return gson.toJson(new FailureEntity(module.name(), msg));
    }

    /**
     * Converts failures information for JSON output in TT Platform.
     *
     * @param module element of {@link org.ssutt.core.dm.TTModule} enum.
     * @param err    element of {@link org.ssutt.core.dm.TTModule} enum.
     * @return JSON representation. See {@link org.ssutt.core.dm.convert.json.entity.FailureEntity}.
     */
    @Override
    public String convertFailure(TTModule module, TTModule err) {
        return gson.toJson(new FailureEntity(module.name(), err.name()));
    }

}


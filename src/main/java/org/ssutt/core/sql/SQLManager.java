/*
Copyright 2014 Plain Solutions

Authors:
   Vlad Slepukhin <slp.vld@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.ssutt.core.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public interface SQLManager {
    static final Logger logger = LogManager.getLogger(SQLManager.class.getName());

    public String getDepartmentTag(String name);

    public String getDepartmentName(String tag);

    public HashMap<String, String> getDepartments();

    public String getGroups(String departmentCode);

    public String getTimetable(String departmentCode, String groupName);

    public boolean fillDepartments(HashMap<String, String> departments);

    public boolean pushExecutioner(String query);

}

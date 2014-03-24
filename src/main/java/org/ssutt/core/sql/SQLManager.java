package org.ssutt.core.sql;

import java.util.HashMap;

/**
 * Created by fau on 24/03/14.
 */
public interface SQLManager {

    public String getDepartmentTag(String name);
    public String getDepartmentName(String tag);


    public HashMap<String, String> getDepartments();

    public String getGroups(String departmentCode);

    public String getTimetable(String departmentCode, String groupName);

    public String fillDepartments(HashMap<String, String> departments);

}

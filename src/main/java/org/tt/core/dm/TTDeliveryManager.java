package org.tt.core.dm;

import org.tt.core.entity.datafetcher.Department;
import org.tt.core.entity.datafetcher.Group;
import org.tt.core.entity.db.TTEntity;
import org.tt.core.sql.AbstractQueries;
import org.tt.core.sql.AbstractSQLManager;
import org.tt.core.sql.ex.NoSuchDepartmentException;
import org.tt.core.sql.ex.NoSuchGroupException;

import java.sql.SQLException;
import java.util.List;

public class TTDeliveryManager {
    private AbstractSQLManager sqlm;

    public TTDeliveryManager(AbstractSQLManager sqlm) {
        this.sqlm = sqlm;
    }

    public List<Department> getDepartments() throws SQLException {
        return sqlm.getDepartments();
    }

    public List<String> getDepartmentTags() throws SQLException {
        return sqlm.getDepartmentTags();
    }

    public String getDepartmentMessage(String departmentTag) throws SQLException { return sqlm.getDepartmentMessage(departmentTag);}

    public List<Group> getGroups(String departmentTag) throws NoSuchDepartmentException, SQLException {
        return sqlm.getGroups(departmentTag);
    }

    public List<Group> getNonEmptyGroups(String departmentTag) throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
        return sqlm.getNonEmptyGroups(departmentTag);
    }

    public TTEntity getTT(String departmentTag, String groupName) throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
        return getTT(sqlm.getGroupID(departmentTag, groupName));
    }

    private TTEntity getTT(int groupID) throws SQLException, NoSuchGroupException {
        return sqlm.getTT(groupID);
    }
}

package org.tt.core.dm;

import org.tt.core.entity.datafetcher.Department;
import org.tt.core.fetch.AbstractDataFetcher;
import org.tt.core.sql.AbstractSQLManager;
import org.tt.core.sql.ex.NoSuchDepartmentException;
import org.tt.core.sql.ex.NoSuchGroupException;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class UpdateManager extends SSUDataManager {

    public UpdateManager() {
    }

    public void checkDepartments() throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
        List<Department> ssuDeps = df.getDepartments();
        List<Department> dbDeps = sqlm.getDepartments();

        for (Department d: dbDeps) {
            if (!ssuDeps.contains(d)) {
                sqlm.deleteDepartment(d);
            }
        }

        for (Department d: ssuDeps) {
            if (!dbDeps.contains(d))
               sqlm.putDepartment(d);
            }

        for (Department sd: ssuDeps) {
            for (Department dbD: dbDeps) {
                if (dbD.getTag().equals(sd.getTag())&&
                        (dbD.getName().equals(sd.getName()))) {
                    if (!(dbD.getMessage().equals(sd.getMessage()))) {
                        sqlm.updateDepartmentMessage(dbD.getTag(), sd.getMessage());
                    }
                }

                if (dbD.getTag().equals(sd.getTag())&&(!dbD.getName().equals(sd.getName()))) {
                    sqlm.updateDepartmentInfo(sd.getName(), sd.getTag(), sd.getMessage(), dbD.getTag());
                }
            }
        }
    }


}

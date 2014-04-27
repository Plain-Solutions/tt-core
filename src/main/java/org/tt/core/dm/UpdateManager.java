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
    private static AbstractSQLManager usqlm;
    private static AbstractDataFetcher udf;
    public UpdateManager() {
    }

    public UpdateManager(AbstractSQLManager usqlm, AbstractDataFetcher udf) {
        this.usqlm = usqlm;
        this.udf = udf;
    }

    public void checkDepartments() throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
        List<Department> ssuDeps = udf.getDepartments();
        List<Department> dbDeps = usqlm.getDepartments();

        for (Department d: dbDeps) {
            if (!ssuDeps.contains(d)) {
                usqlm.deleteDepartment(d);
            }
        }

        for (Department d: ssuDeps) {
            if (!dbDeps.contains(d))
               usqlm.putDepartment(d);
            }

        for (Department sd: ssuDeps) {
            for (Department dbD: dbDeps) {
                if (dbD.getTag().equals(sd.getTag())&&
                        (dbD.getName().equals(sd.getName()))) {
                    if (!(dbD.getMessage().equals(sd.getMessage()))) {
                        usqlm.updateDepartmentMessage(dbD.getTag(), sd.getMessage());
                    }
                }

                if (dbD.getTag().equals(sd.getTag())&&(!dbD.getName().equals(sd.getName()))) {
                    usqlm.updateDepartmentInfo(sd.getName(), sd.getTag(), sd.getMessage(), dbD.getTag());
                }
            }
        }
    }


}

package org.tt.core.dm;

import org.tt.core.entity.datafetcher.Department;
import org.tt.core.entity.datafetcher.Group;
import org.tt.core.entity.datafetcher.Lesson;
import org.tt.core.fetch.AbstractDataFetcher;
import org.tt.core.sql.AbstractQueries;
import org.tt.core.sql.AbstractSQLManager;
import org.tt.core.sql.ex.NoSuchDepartmentException;
import org.tt.core.sql.ex.NoSuchGroupException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UpdateManager extends SSUDataManager {
    
    public UpdateManager(AbstractSQLManager sqlm, AbstractQueries qrs, AbstractDataFetcher df, AbstractDataConverter dconv) {
            super(sqlm, qrs, df, dconv);
    }

    public void checkDepartments() throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
        System.out.println("Checking departments for update!");

        List<Department> ssuDeps = df.getDepartments();
        List<Department> dbDeps = sqlm.getDepartments();
        
        for (Department d: dbDeps) {
                if (!(ssuDeps.contains(d))) {
                    System.out.println("Removed: "+d.getTag());
                    sqlm.deleteDepartment(d);
                }
            }

            for (Department d: ssuDeps) {
                if (!dbDeps.contains(d)) {
                    System.out.println("Added: " + d.getTag());
                    sqlm.putDepartment(d);
                    super.putDepartmentGroups(d.getTag()); //needed to fetch, won't use df directly
                    for (Group g : sqlm.getGroups(d.getTag())) { //represented in JSON format, needs conversion, accessing db
                        super.putTT(d.getTag(), g.getName()); //same is here
                    }
                }
            }

        for (Department sd: ssuDeps) {
            for (Department dbD: dbDeps) {
                if (dbD.getTag().equals(sd.getTag())&&
                        (dbD.getName().equals(sd.getName()))) {
                    if (!(dbD.getMessage().equals(sd.getMessage()))) {
                        System.out.println("Message updated for: "+sd.getTag());
                        sqlm.updateDepartmentMessage(dbD.getTag(), sd.getMessage()); //should not be accessible from ADM
                    }
                }

                if (dbD.getTag().equals(sd.getTag())&&(!dbD.getName().equals(sd.getName()))) {
                    System.out.println("Info updated for: "+ dbD.getTag());
                    sqlm.updateDepartmentInfo(sd.getName(), sd.getTag(), sd.getMessage(), dbD.getTag());
                }
            }
        }
    }

    public void checkGroups() throws SQLException, NoSuchDepartmentException, NoSuchGroupException {
        for(Department dep: sqlm.getDepartments()) {
            System.out.println("Checking groups in "+dep.getTag());
            List<Group> ssuGroups = df.getGroups(dep.getTag());
            List<Group> dbGroups = sqlm.getGroups(dep.getTag());

            for (Group g: dbGroups) {
                System.out.println(g.getName());
            }

            System.out.println("----");

            for (Group g: ssuGroups) {
                System.out.println(g.getName());
            }

            System.out.printf("-----\n");

            for (Group g: dbGroups) {
                if (!(ssuGroups.contains(g))) {
                    System.out.println("Removed: "+g.getName()+"@"+dep.getTag());
                    sqlm.deleteGroupFromDepartment(dep, g);
                }
            }

            for (Group g: ssuGroups) {
                if (!(dbGroups.contains(g))) {
                    System.out.println("Added: "+g.getName()+"@"+dep.getTag());
                        sqlm.putGroup(g, dep.getTag());
                        super.putTT(dep.getTag(), g.getName());
                    }
                }

        }
    }

    public void checkTimetables() throws SQLException, NoSuchDepartmentException, IOException, NoSuchGroupException {
        for (Department dep: sqlm.getDepartments()) {
            for (Group gr: sqlm.getGroups(dep.getTag())) {
                List<List<Lesson>> ssuTT = df.getTT(dep.getTag(), gr.getName());
                List<List<Lesson>> dbTT = sqlm.getLessonList(sqlm.getGroupID(dep.getTag(), gr.getName()));
                
            }
        }
    }


}

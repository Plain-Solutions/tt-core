/*
 * Copyright 2014 Plain Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

public class TTUpdateManager extends SSUDataManager {

    public TTUpdateManager(AbstractSQLManager sqlm, AbstractQueries qrs, AbstractDataFetcher df) {
        super(sqlm, qrs, df);
    }

    public void checkDepartments() throws SQLException, NoSuchDepartmentException, NoSuchGroupException, IOException {
        System.out.println("Checking departments for update!");

        List<Department> ssuDeps = df.getDepartments();
        List<Department> dbDeps = sqlm.getDepartments();

        for (Department d : dbDeps) {
            if (!(ssuDeps.contains(d))) {
                System.out.println("Removed: " + d.getTag());
                sqlm.deleteDepartment(d);
            }
        }

        for (Department d : ssuDeps) {
            if (!dbDeps.contains(d)) {
                System.out.println("Added: " + d.getTag());
                sqlm.putDepartment(d);
                super.putDepartmentGroups(d.getTag()); //needed to fetch, won't use df directly
                for (Group g : sqlm.getGroups(d.getTag())) { //represented in JSON format, needs conversion, accessing db
                    super.putTT(d.getTag(), g.getName()); //same is here
                }
            }
        }

        for (Department sd : ssuDeps) {
            for (Department dbD : dbDeps) {
                if (dbD.getTag().equals(sd.getTag()) &&
                        (dbD.getName().equals(sd.getName()))) {
                    if (!(dbD.getMessage().equals(sd.getMessage()))) {
                        System.out.println("Message updated for: " + sd.getTag());
                        sqlm.updateDepartmentMessage(dbD.getTag(), sd.getMessage()); //should not be accessible from ADM
                    }
                }

                if (dbD.getTag().equals(sd.getTag()) && (!dbD.getName().equals(sd.getName()))) {
                    System.out.println("Info updated for: " + dbD.getTag());
                    sqlm.updateDepartmentInfo(sd.getName(), sd.getTag(), sd.getMessage(), dbD.getTag());
                }
            }
        }
        System.out.println("Finished updating department data.");
    }

    public void checkGroups() throws SQLException, NoSuchDepartmentException, NoSuchGroupException, IOException {
        for (Department dep : sqlm.getDepartments()) {
            System.out.println("Checking groups in " + dep.getTag());
            List<Group> ssuGroups = df.getGroups(dep.getTag());
            List<Group> dbGroups = sqlm.getGroups(dep.getTag());

            for (Group g : dbGroups) {
                if (!(ssuGroups.contains(g))) {
                    System.out.println("Removed: " + g.getName() + "@" + dep.getTag());
                    sqlm.deleteGroupFromDepartment(dep, g);
                }
            }

            for (Group g : ssuGroups) {
                if (!(dbGroups.contains(g))) {
                    System.out.println("Added: " + g.getName() + "@" + dep.getTag());
                    sqlm.putGroup(g, dep.getTag());
                    super.putTT(dep.getTag(), g.getName());
                }
            }

        }
        System.out.println("Finished updating groups data.");
    }

    public void checkTimetables() throws SQLException, NoSuchDepartmentException, IOException, NoSuchGroupException {
        System.out.println("Checking timetables!");

        for (Department dep : sqlm.getDepartments()) {
            for (Group gr : sqlm.getGroups(dep.getTag())) {
                List<List<Lesson>> ssuTT = df.getTT(dep.getTag(), gr.getName());
                List<List<Lesson>> dbTT = sqlm.getLessonList(sqlm.getGroupID(dep.getTag(), gr.getName()));

                int groupID = sqlm.getGroupID(dep.getTag(), gr.getName());

                for (int i = 0; i < 6; i++) {
                    for (Lesson l : dbTT.get(i)) {
                        if (!(ssuTT.get(i).contains(l))) {
                            deleteLesson(l, i + 1, groupID);
                            System.out.println(String.format("Removed class on %d day, %d sequence and %s parity in %s@%s ",
                                    i + 1, l.getSequence(), l.getParity(), gr.getName(), dep.getTag()));
                        }
                    }

                    for (Lesson l : ssuTT.get(i)) {
                        if (!(dbTT.get(i).contains(l))) {
                            putLesson(l, i + 1, groupID);
                            System.out.println(String.format("Added class on %d day, %d sequence and %s parity in %s@%s ",
                                    i + 1, l.getSequence(), l.getParity(), gr.getName(), dep.getTag()));
                        }
                    }
                }
                System.out.println(String.format("Checked %s@%s", gr.getName(), dep.getTag()));
            }
        }
        System.out.println("Finished updating timetables.");
    }

    public void flushDatabase() throws SQLException {
        sqlm.flushDatabase();
    }

    private void deleteLesson(Lesson l, int day, int groupID) throws SQLException {
        if (!l.isEmpty()) {
            int sequence = l.getSequence();
            int parityID = sqlm.getParityID(l.getParity());
            int subgrpID = sqlm.putSubGroup(groupID, l.getSubgroup());
            int activityID = sqlm.getActivityID(l.getActivity());
            int subjectID = sqlm.putSubject(l.getSubject());
            int teacherID = sqlm.putTeacher(l.getTeacher());
            int locationID = sqlm.putLocation(l.getBuilding(), l.getRoom());
            int datatimeID = sqlm.putDateTime(parityID, sequence, day);

            sqlm.deleteLesson(groupID, datatimeID, activityID, subjectID, subgrpID, teacherID,
                    locationID);
        }
    }

    private void putLesson(Lesson l, int day, int groupID) throws SQLException {
        if (!l.isEmpty()) {
            int sequence = l.getSequence();
            int parityID = sqlm.getParityID(l.getParity());
            int subgrpID = sqlm.putSubGroup(groupID, l.getSubgroup());
            int activityID = sqlm.getActivityID(l.getActivity());
            int subjectID = sqlm.putSubject(l.getSubject());
            int teacherID = sqlm.putTeacher(l.getTeacher());
            int locationID = sqlm.putLocation(l.getBuilding(), l.getRoom());
            int datatimeID = sqlm.putDateTime(parityID, sequence, day);

            sqlm.putLessonRecord(groupID, datatimeID, activityID, subjectID, subgrpID, teacherID,
                    locationID, l.getTimestamp());
        }
    }
}
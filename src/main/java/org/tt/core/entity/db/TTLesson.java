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
package org.tt.core.entity.db;

import java.util.ArrayList;
import java.util.List;

/**
 * TTLesson is a entity to provide data from the database to the end-user in a format, which can be
 * easily serialised.
 *
 * @author Vlad Slepukhin
 * @since 2.0.0
 */
public class TTLesson {
    /**
     * The parity name: nominator, denominator (even/odd) or full (both).
     */
    private String parity;
    /**
     * The order of the class during the day.
     */
    private int sequence;

    /**
     * The list of lesson records.
     */
    private List<TTLessonRecord> records;

    /**
     * The data which is common for the lessons <b>independent from subgroups or location splitting</b>.
     */
    public class TTLessonRecord {
        /**
         * The type of the activity: lecture, practice or laboratory.
         */
        private String activity;
        /**
         * The name of the subject.
         */
        private String subject;
        /**
         * The list of teachers, subgroups and locations.
         */
        private List<TTClassRoomEntity> classRoomEntities;
        /**
         * Parity for API v2
         */
        private String parity;

        private TTLessonRecord() {
            classRoomEntities = new ArrayList<>();
        }

        private TTLessonRecord(String activity, String subject) {
            classRoomEntities = new ArrayList<>();
            this.activity = activity;
            this.subject = subject;
            this.parity = null;
        }

        public String getActivity() {
            return activity;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public List<TTClassRoomEntity> getClassRoomEntities() {
            return classRoomEntities;
        }

        public void setClassRoomEntities(List<TTClassRoomEntity> classRoomEntities) {
            this.classRoomEntities = classRoomEntities;
        }

        public String getParity() {
            return parity;
        }

        public void setParity(String parity) {
            this.parity = parity;
        }

        public void appendClassRoomEntity(TTClassRoomEntity cre) {
            classRoomEntities.add(cre);
        }
    }

    /**
     * The subgroup-dependent information. Contains teacher name, location and the subgroup.
     */
    public class TTClassRoomEntity {
        private String subgroup;
        private String teacher;
        private String building;
        private String room;

        private TTClassRoomEntity() {
        }

        private TTClassRoomEntity(String subgroup, String teacher, String building, String room) {
            this.subgroup = subgroup;
            this.teacher = teacher;
            this.building = building;
            this.room = room;
        }

        public String getSubgroup() {
            return subgroup;
        }

        public void setSubgroup(String subgroup) {
            this.subgroup = subgroup;
        }

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }

        public String getBuilding() {
            return building;
        }

        public void setBuilding(String building) {
            this.building = building;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }
    }

    public TTLesson() {
        records = new ArrayList<>();
    }

    public TTLesson(String parity, int sequence) {
        records = new ArrayList<>();

        this.parity = parity;
        this.sequence = sequence;
    }

    public String getParity() {
        return parity;
    }

    public void setParity(String parity) {
        this.parity = parity;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    /**
     * Add a lesson to the list during this day.
     *
     * @param activity Performed activity.
     * @param subject  Subject name.
     * @param subgroup Subgroup name.
     * @param teacher  Teacher name.
     * @param building Building (part of location).
     * @param room     Room (part of location).
     * @since 2.0.0
     */
    public void append(String activity, String subject, String subgroup, String teacher, String building, String room) {
        //optimizing

        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getSubject().equals(subject)) {
                records.get(i).appendClassRoomEntity(createCRE(subgroup, teacher, building, room));
                return;
            }
        }
        //no record appeared
        TTLessonRecord record = new TTLessonRecord();
        record.setActivity(activity);
        record.setSubject(subject);
        record.appendClassRoomEntity(createCRE(subgroup, teacher, building, room));
        records.add(record);
    }

    /**
     * Add a lesson to the list during this day in faster way.
     * @param record The whole information.
     * @since 2.1.1
     */
    public void append (TTLessonRecord record) {
        records.add(record);
    }

    /**
     * Utility method.
     *
     * @param subgroup Subgroup name.
     * @param teacher  Teacher name.
     * @param building Building (part of location).
     * @param room     Room (part of location).
     * @return the instance of {@link org.tt.core.entity.db.TTLesson.TTClassRoomEntity}
     * @since 2.0.0
     */
    private TTClassRoomEntity createCRE(String subgroup, String teacher, String building, String room) {
        TTClassRoomEntity cre = new TTClassRoomEntity();
        cre.setSubgroup(subgroup);
        cre.setTeacher(teacher);
        cre.setBuilding(building);
        cre.setRoom(room);

        return cre;
    }

    public List<TTLessonRecord> getRecords() {
        return records;
    }
}

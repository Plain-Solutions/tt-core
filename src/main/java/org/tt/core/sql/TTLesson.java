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
package org.tt.core.sql;

import java.util.ArrayList;
import java.util.List;

public class TTLesson {
    private String parity;
    private int sequence;

    private List<TTLessonRecord> records;

    public class TTLessonRecord {
        private String activity;
        private String subject;
        private List<TTClassRoomEntity> classRoomEntities;

        private TTLessonRecord() {
            classRoomEntities = new ArrayList<>();
        }

        private TTLessonRecord(String activity, String subject) {
            classRoomEntities = new ArrayList<>();
            this.activity = activity;
            this.subject = subject;
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

        public void appendClassRoomEntity(TTClassRoomEntity cre) {
           classRoomEntities.add(cre);
        }
    }

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

    public void append(String activity, String subject, String subgroup, String teacher, String building, String room) {
        //optimizing

        for (int i=0; i < records.size(); i++) {
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

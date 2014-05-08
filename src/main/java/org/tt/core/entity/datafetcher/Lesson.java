/*
* Copyright 2014 Plain Solutions
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.tt.core.entity.datafetcher;

public class Lesson {
    private int sequence;
    private String parity;
    private String subgroup;
    private String activity;
    private String subject;
    private String teacher;
    private String building;
    private String room;
    private long timestamp;

    public Lesson() {
    }

    public Lesson(int sequence, String parity, String subgroup, String activity, String subject, String teacher, String building, String room, long timestamp) {
        this.sequence = sequence;
        this.parity = parity;
        this.subgroup = subgroup;
        this.activity = activity;
        this.subject = subject;
        this.teacher = teacher;
        this.building = building;
        this.room = room;
        this.timestamp = timestamp;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getParity() {
        return parity;
    }

    public void setParity(String parity) {
        this.parity = parity;
    }

    public String getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(String subgroup) {
        this.subgroup = subgroup;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isEmpty() {
        return sequence == -1 &&
                parity.equals("") &&
                subgroup.equals("") &&
                activity.equals("") &&
                subject.equals("") &&
                teacher.equals("") &&
                building.equals("") &&
                room.equals("") &&
                timestamp == -1L;
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o.getClass() == Lesson.class && sequence == ((Lesson) o).getSequence() &&
                subject.equals(((Lesson) o).getSubject()) && timestamp == ((Lesson) o).getTimestamp() &&
                parity.equals(((Lesson) o).getParity()) && subgroup.equals(((Lesson) o).getSubgroup()) && activity.equals(((Lesson) o).getActivity()) &&
                teacher.equals(((Lesson) o).getTeacher()) && building.equals(((Lesson) o).getBuilding()) &&
                room.equals(((Lesson) o).getRoom());
    }

    @Override
    public String toString() {
        return String.format("Sequence: %d\n" +
                "Parity: %s\n" +
                "Activity: %s\n" +
                "Subject: %s\n" +
                "Teacher: %s\n" +
                "Subgroup: %s\n" +
                "Building: %s\n" +
                "Room: %s\n" +
                "Timestamp: %d\n", sequence, parity, activity, subject, teacher, subgroup, building, room, timestamp);
    }
}
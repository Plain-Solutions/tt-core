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

/**
 * Lesson is a entity, containing all the required parameters of any lesson.
 *
 * @author Vlad Slepukhin
 * @author Sevak Avetisyan
 * @since 2.0.0
 */
public class Lesson {
    /**
     * The order during the day from 1st class till the end.
     */
    private int sequence;
    /**
     * The week order: even, odd (nominator or denominator) or both (full).
     */
    private String parity;
    /**
     * A subgroup of the lesson.
     */
    private String subgroup;
    /**
     * Performed activity: lecture, practice or laboratory.
     */
    private String activity;
    /**
     * The name of the subject.
     */
    private String subject;
    /**
     * Compiled name of the teacher.
     */
    private String teacher;
    /**
     * Building (part of location).
     */
    private String building;
    /**
     * Room or auditory (part of location).
     */
    private String room;
    /**
     * Update information. Required for updating only lessons, which were changed with
     * faster analysis.
     */
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

    /**
     * Check for emptiness. Used in fetching even empty lessons, but not storing them.
     * @return <code>true</code> if contains no data, otherwise <code>false</code>.
     * @since 2.0.0
     */
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

    /**
     * Checking for equality. Used in updating.
     * @param l The checked lesson.
     * @return <code>true</code> if equals, otherwise <code>false</code>.
     * @since 2.0.0
     */
    @Override
    public boolean equals(Object l) {
        return l != null && l.getClass() == Lesson.class && timestamp == ((Lesson) l).getTimestamp() && sequence == ((Lesson) l).getSequence() &&
                subject.equals(((Lesson) l).getSubject()) &&
                parity.equals(((Lesson) l).getParity()) && subgroup.equals(((Lesson) l).getSubgroup()) && activity.equals(((Lesson) l).getActivity()) &&
                teacher.equals(((Lesson) l).getTeacher()) && building.equals(((Lesson) l).getBuilding()) &&
                room.equals(((Lesson) l).getRoom());
    }

    /**
     * Fancy output for tracing.
     * @return All the fields of the calls formatted as <code>Name: value\n</code>
     * @since 2.1.0
     */
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
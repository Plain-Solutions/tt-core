package org.tt.core.sql;

public class DBLesson {
    private String day;
    private String parity;
    private int sequence;
    private String activity;
    private String subject;
    private String subgroup;
    private String teacher;
    private String building;
    private String room;

    public DBLesson() {
    }

    public DBLesson(String parity, String day, int sequence,
                    String activity, String subject, String subgroup,
                    String teacher, String building, String room) {
        this.parity = parity;
        this.day = day;
        this.sequence = sequence;
        this.activity = activity;
        this.subject = subject;
        this.subgroup = subgroup;
        this.teacher = teacher;
        this.building = building;
        this.room = room;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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

    @Override
    public String toString() {
        return String.format("day: %s\n" +
                "parity: %s\n" +
                "sequence: %d\n" +
                "activity: %s\n" +
                "subject: %s\n" +
                "subgroup: %s\n" +
                "teacher: %s\n" +
                "location: %s %s\n", day, parity, sequence, activity,
                subject, subgroup, teacher, building, room);
    }
}

package org.tt.core.sql;

public class DBLesson {
    private String day;
    private String parity;
    private int sequence;
    private String subgroup;
    private String subject;
    private String teacher;

    public DBLesson() {
    }

    public DBLesson(String day, String parity, int sequence, String subgroup, String subject, String teacher) {
        this.day = day;
        this.parity = parity;
        this.sequence = sequence;
        this.subgroup = subgroup;
        this.subject = subject;
        this.teacher = teacher;
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

    public String getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(String subgroup) {
        this.subgroup = subgroup;
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
}

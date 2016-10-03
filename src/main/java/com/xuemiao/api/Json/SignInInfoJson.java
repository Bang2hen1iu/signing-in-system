package com.xuemiao.api.Json;

/**
 * Created by dzj on 10/3/2016.
 */
public class SignInInfoJson {
    private String studentId;
    private String name;
    private String startMorning;
    private String endMorning;
    private String startAfternoon;
    private String endAfternoon;
    private String startNight;
    private String endNight;
    private String currentDayCourses;
    private String startAbsence;
    private String endAbsence;
    private String absenceReason;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartMorning() {
        return startMorning;
    }

    public void setStartMorning(String startMorning) {
        this.startMorning = startMorning;
    }

    public String getEndMorning() {
        return endMorning;
    }

    public void setEndMorning(String endMorning) {
        this.endMorning = endMorning;
    }

    public String getStartAfternoon() {
        return startAfternoon;
    }

    public void setStartAfternoon(String startAfternoon) {
        this.startAfternoon = startAfternoon;
    }

    public String getEndAfternoon() {
        return endAfternoon;
    }

    public void setEndAfternoon(String endAfternoon) {
        this.endAfternoon = endAfternoon;
    }

    public String getStartNight() {
        return startNight;
    }

    public void setStartNight(String startNight) {
        this.startNight = startNight;
    }

    public String getEndNight() {
        return endNight;
    }

    public void setEndNight(String endNight) {
        this.endNight = endNight;
    }

    public String getCurrentDayCourses() {
        return currentDayCourses;
    }

    public void setCurrentDayCourses(String currentDayCourses) {
        this.currentDayCourses = currentDayCourses;
    }

    public String getStartAbsence() {
        return startAbsence;
    }

    public void setStartAbsence(String startAbsence) {
        this.startAbsence = startAbsence;
    }

    public String getEndAbsence() {
        return endAbsence;
    }

    public void setEndAbsence(String endAbsence) {
        this.endAbsence = endAbsence;
    }

    public String getAbsenceReason() {
        return absenceReason;
    }

    public void setAbsenceReason(String absenceReason) {
        this.absenceReason = absenceReason;
    }
}

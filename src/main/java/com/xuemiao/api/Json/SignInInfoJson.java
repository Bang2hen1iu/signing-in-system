package com.xuemiao.api.Json;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by dzj on 10/3/2016.
 */
public class SignInInfoJson {
    private String studentId;
    private String name;
    private Timestamp startMorning;
    private Timestamp endMorning;
    private Timestamp startAfternoon;
    private Timestamp endAfternoon;
    private Timestamp startNight;
    private Timestamp endNight;
    private String currentDayCourses;
    private Timestamp startAbsence;
    private Timestamp endAbsence;
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

    public Timestamp getStartMorning() {
        return startMorning;
    }

    public void setStartMorning(Timestamp startMorning) {
        this.startMorning = startMorning;
    }

    public Timestamp getEndMorning() {
        return endMorning;
    }

    public void setEndMorning(Timestamp endMorning) {
        this.endMorning = endMorning;
    }

    public Timestamp getEndAfternoon() {
        return endAfternoon;
    }

    public void setEndAfternoon(Timestamp endAfternoon) {
        this.endAfternoon = endAfternoon;
    }

    public Timestamp getStartNight() {
        return startNight;
    }

    public void setStartNight(Timestamp startNight) {
        this.startNight = startNight;
    }

    public Timestamp getEndNight() {
        return endNight;
    }

    public void setEndNight(Timestamp endNight) {
        this.endNight = endNight;
    }

    public Timestamp getStartAbsence() {
        return startAbsence;
    }

    public void setStartAbsence(Timestamp startAbsence) {
        this.startAbsence = startAbsence;
    }

    public Timestamp getEndAbsence() {
        return endAbsence;
    }

    public void setEndAbsence(Timestamp endAbsence) {
        this.endAbsence = endAbsence;
    }

    public Timestamp getStartAfternoon() {
        return startAfternoon;
    }

    public void setStartAfternoon(Timestamp startAfternoon) {
        this.startAfternoon = startAfternoon;
    }

    public String getCurrentDayCourses() {
        return currentDayCourses;
    }

    public void setCurrentDayCourses(String currentDayCourses) {
        this.currentDayCourses = currentDayCourses;
    }

    public String getAbsenceReason() {
        return absenceReason;
    }

    public void setAbsenceReason(String absenceReason) {
        this.absenceReason = absenceReason;
    }
}

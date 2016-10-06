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
    private String startMorningSignatureImgName;
    private String endMorningSignatureImgName;
    private String startAfternoonSignatureImgName;
    private String endAfternoonSignatureImgName;
    private String startNightSignatureImgName;
    private String endNightSignatureImgName;

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

    public String getStartMorningSignatureImgName() {
        return startMorningSignatureImgName;
    }

    public void setStartMorningSignatureImgName(String startMorningSignatureImgName) {
        this.startMorningSignatureImgName = startMorningSignatureImgName;
    }

    public String getEndMorningSignatureImgName() {
        return endMorningSignatureImgName;
    }

    public void setEndMorningSignatureImgName(String endMorningSignatureImgName) {
        this.endMorningSignatureImgName = endMorningSignatureImgName;
    }

    public String getStartAfternoonSignatureImgName() {
        return startAfternoonSignatureImgName;
    }

    public void setStartAfternoonSignatureImgName(String startAfternoonSignatureImgName) {
        this.startAfternoonSignatureImgName = startAfternoonSignatureImgName;
    }

    public String getEndAfternoonSignatureImgName() {
        return endAfternoonSignatureImgName;
    }

    public void setEndAfternoonSignatureImgName(String endAfternoonSignatureImgName) {
        this.endAfternoonSignatureImgName = endAfternoonSignatureImgName;
    }

    public String getStartNightSignatureImgName() {
        return startNightSignatureImgName;
    }

    public void setStartNightSignatureImgName(String startNightSignatureImgName) {
        this.startNightSignatureImgName = startNightSignatureImgName;
    }

    public String getEndNightSignatureImgName() {
        return endNightSignatureImgName;
    }

    public void setEndNightSignatureImgName(String endNightSignatureImgName) {
        this.endNightSignatureImgName = endNightSignatureImgName;
    }
}

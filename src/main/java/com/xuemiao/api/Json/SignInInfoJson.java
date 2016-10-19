package com.xuemiao.api.Json;

import com.xuemiao.model.pdm.SignInInfoRecordEntity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by dzj on 10/3/2016.
 */
public class SignInInfoJson implements Serializable {
    private String studentId;
    private String name;
    private List<SignInInfoRecordEntity> signInInfoRecordEntities;
    private List<SignInInfoCoursesInfo> signInInfoCoursesInfoList;
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

    public List<SignInInfoRecordEntity> getSignInInfoRecordEntities() {
        return signInInfoRecordEntities;
    }

    public void setSignInInfoRecordEntities(List<SignInInfoRecordEntity> signInInfoRecordEntities) {
        this.signInInfoRecordEntities = signInInfoRecordEntities;
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

    public List<SignInInfoCoursesInfo> getSignInInfoCoursesInfoList() {
        return signInInfoCoursesInfoList;
    }

    public void setSignInInfoCoursesInfoList(List<SignInInfoCoursesInfo> signInInfoCoursesInfoList) {
        this.signInInfoCoursesInfoList = signInInfoCoursesInfoList;
    }

    public String getAbsenceReason() {
        return absenceReason;
    }

    public void setAbsenceReason(String absenceReason) {
        this.absenceReason = absenceReason;
    }

}

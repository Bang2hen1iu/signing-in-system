package com.xuemiao.api.Json;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by dzj on 10/3/2016.
 */
public class AbsenceReasonJson {
    private Date operDate;
    private Long studentId;
    private String absenceReason;
    private Timestamp startAbsence;
    private Timestamp endAbsence;

    public Date getOperDate() {
        return operDate;
    }

    public void setOperDate(Date operDate) {
        this.operDate = operDate;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getAbsenceReason() {
        return absenceReason;
    }

    public void setAbsenceReason(String absenceReason) {
        this.absenceReason = absenceReason;
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
}

package com.xuemiao.model.pdm;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by dzj on 9/30/2016.
 */
@Entity
@IdClass(value = StudentIdAndOperDateKey.class)
@Table(name = "absence")
public class AbsenceEntity implements Serializable {
    @Id
    private Long studentId;
    @Id
    private DateTime operDate;
    @Column(name = "start_absence")
    private Timestamp startAbsence;
    @Column(name = "end_absence")
    private Timestamp endAbsence;
    @Column(name = "absence_reason")
    private String absenceReason;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public DateTime getOperDate() {
        return operDate;
    }

    public void setOperDate(DateTime operDate) {
        this.operDate = operDate;
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

    public String getAbsenceReason() {
        return absenceReason;
    }

    public void setAbsenceReason(String absenceReason) {
        this.absenceReason = absenceReason;
    }
}

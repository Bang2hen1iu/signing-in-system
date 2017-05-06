package com.xuemiao.model.pdm;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by dzj on 9/30/2016.
 */
@Entity
@Table(name = "absence")
public class AbsenceEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long absenceId;
    @Column(name = "sign_in_info_id")
    private Long signInInfoId;
    @Column(name = "start_absence")
    private Timestamp startAbsence;
    @Column(name = "end_absence")
    private Timestamp endAbsence;
    @Column(name = "absence_reason")
    private String absenceReason;
    @Column(name = "is_make_up")
    private boolean isMakeUp;

    public Long getAbsenceId() {
        return absenceId;
    }

    public void setAbsenceId(Long absenceId) {
        this.absenceId = absenceId;
    }

    public Long getSignInInfoId() {
        return signInInfoId;
    }

    public void setSignInInfoId(Long signInInfoId) {
        this.signInInfoId = signInInfoId;
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

    public boolean isMakeUp() {
        return isMakeUp;
    }

    public void setMakeUp(boolean makeUp) {
        isMakeUp = makeUp;
    }
}

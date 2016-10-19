package com.xuemiao.model.pdm;

import com.xuemiao.model.pdm.primaryKey.SignInInfoRecordPK;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by root on 16-10-17.
 */
@Entity
@IdClass(value = SignInInfoRecordPK.class)
@Table(name = "sign_in_info_record")
public class SignInInfoRecordEntity {
    @Id
    private Long signInInfoId;
    @Id
    private Timestamp startTime;
    @Column(name = "end_time")
    private Timestamp endTime;

    public Long getSignInInfoId() {
        return signInInfoId;
    }

    public void setSignInInfoId(Long signInInfoId) {
        this.signInInfoId = signInInfoId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}

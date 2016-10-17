package com.xuemiao.model.pdm;

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
    private Long signInId;
    @Id
    private Timestamp startTime;
    @Column(name = "end_time")
    private Timestamp endTime;

    public Long getSignInId() {
        return signInId;
    }

    public void setSignInId(Long signInId) {
        this.signInId = signInId;
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

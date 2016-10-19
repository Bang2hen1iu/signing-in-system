package com.xuemiao.model.pdm.primaryKey;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by root on 16-10-17.
 */
@Embeddable
public class SignInInfoRecordPK implements Serializable{
    @Column(name = "sign_in_info_id")
    private Long signInInfoId;
    @Column(name = "start_time")
    private Timestamp startTime;

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
}

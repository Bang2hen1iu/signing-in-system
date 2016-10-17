package com.xuemiao.model.pdm;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by root on 16-10-17.
 */
@Embeddable
public class SignInInfoRecordPK implements Serializable{
    @Column(name = "sign_in_id")
    private Long signInId;
    @Column(name = "start_time")
    private Timestamp startTime;

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
}

package com.xuemiao.model.pdm.primaryKey;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by root on 16-10-19.
 */
@Embeddable
public class FingerprintPK implements Serializable{
    @Column(name = "student_id")
    private Long studentId;
    @Column(name = "token")
    private String token;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

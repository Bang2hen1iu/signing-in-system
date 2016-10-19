package com.xuemiao.model.pdm;

import com.xuemiao.model.pdm.primaryKey.FingerprintPK;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by root on 16-10-19.
 */
@Entity
@Table(name = "fingerprint")
@IdClass(FingerprintPK.class)
public class FingerprintEntity implements Serializable {
    @Id
    private Long studentId;
    @Id
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

package com.xuemiao.api.Json;

/**
 * Created by root on 16-10-17.
 */
public class RegisterStudentJson {
    private Long studentId;
    private String fingerprint;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}

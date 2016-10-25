package com.xuemiao.api.Json;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dzj on 10/3/2016.
 */
public class SignInInfoJson implements Serializable {
    private String studentId;
    private String name;
    private List<SignInInfoTimeSegment> signInInfoTimeSegments;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SignInInfoTimeSegment> getSignInInfoTimeSegments() {
        return signInInfoTimeSegments;
    }

    public void setSignInInfoTimeSegments(List<SignInInfoTimeSegment> signInInfoTimeSegments) {
        this.signInInfoTimeSegments = signInInfoTimeSegments;
    }
}

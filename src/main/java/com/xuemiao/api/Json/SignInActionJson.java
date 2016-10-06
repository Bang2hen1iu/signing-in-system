package com.xuemiao.api.Json;

import java.sql.Date;

/**
 * Created by dzj on 10/3/2016.
 */
public class SignInActionJson {
    private Long studentId;
    private Date operDate;
    private int signInOrder;
    private String imageData;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Date getOperDate() {
        return operDate;
    }

    public void setOperDate(Date operDate) {
        this.operDate = operDate;
    }

    public int getSignInOrder() {
        return signInOrder;
    }

    public void setSignInOrder(int signInOrder) {
        this.signInOrder = signInOrder;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}

package com.xuemiao.api.Json;

import java.sql.Date;

/**
 * Created by dzj on 10/4/2016.
 */
public class DutyStudentJson {
    private Long studentId;
    private String name;
    private Date operDate;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getOperDate() {
        return operDate;
    }

    public void setOperDate(Date operDate) {
        this.operDate = operDate;
    }
}

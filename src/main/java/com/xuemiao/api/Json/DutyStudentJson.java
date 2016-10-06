package com.xuemiao.api.Json;

import java.sql.Date;

/**
 * Created by dzj on 10/4/2016.
 */
public class DutyStudentJson {
    private Long studentId;
    private String name;
    private Date startDate;
    private Date endDate;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}

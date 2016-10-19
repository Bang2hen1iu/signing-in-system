package com.xuemiao.model.pdm;

import com.xuemiao.model.pdm.primaryKey.DutyStudentPK;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by dzj on 9/30/2016.
 */
@Entity
@IdClass(value = DutyStudentPK.class)
@Table(name = "duty_student")
public class DutyStudentEntity implements Serializable {
    @Id
    private Long studentId;
    @Id
    private Date startDate;
    @Id
    private Date endDate;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
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

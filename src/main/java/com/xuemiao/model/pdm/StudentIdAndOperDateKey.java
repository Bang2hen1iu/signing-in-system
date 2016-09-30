package com.xuemiao.model.pdm;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by dzj on 9/30/2016.
 */
@Embeddable
public class StudentIdAndOperDateKey implements Serializable{
    @Column(name = "student_id")
    private String studentId;
    @Column(name = "oper_date")
    private Date operDate;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Date getOperDate() {
        return operDate;
    }

    public void setOperDate(Date operDate) {
        this.operDate = operDate;
    }
}

package com.xuemiao.model.pdm;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by dzj on 9/30/2016.
 */
@Embeddable
public class StudentIdAndOperDateKey implements Serializable {
    @Column(name = "student_id")
    private Long studentId;
    @Column(name = "oper_date")
    private Date operDate;

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
}

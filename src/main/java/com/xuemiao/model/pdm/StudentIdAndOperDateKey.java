package com.xuemiao.model.pdm;

import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by dzj on 9/30/2016.
 */
@Embeddable
public class StudentIdAndOperDateKey implements Serializable {
    @Column(name = "student_id")
    private Long studentId;
    @Column(name = "oper_date")
    private DateTime operDate;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public DateTime getOperDate() {
        return operDate;
    }

    public void setOperDate(DateTime operDate) {
        this.operDate = operDate;
    }
}

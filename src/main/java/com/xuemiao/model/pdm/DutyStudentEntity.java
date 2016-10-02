package com.xuemiao.model.pdm;

import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by dzj on 9/30/2016.
 */
@Entity
@IdClass(value = StudentIdAndOperDateKey.class)
@Table(name = "duty_student")
public class DutyStudentEntity implements Serializable {
    @Id
    private Long studentId;
    @Id
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

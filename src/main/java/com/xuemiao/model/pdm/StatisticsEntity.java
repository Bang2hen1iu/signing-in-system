package com.xuemiao.model.pdm;

import com.xuemiao.model.pdm.primaryKey.StudentIdAndOperDateKey;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by dzj on 10/1/2016.
 */
@Entity
@IdClass(value = StudentIdAndOperDateKey.class)
@Table(name = "statistics")
public class StatisticsEntity {
    @Id
    private Long studentId;
    @Id
    private Date operDate;
    @Column(name = "stay_lab_time")
    private double stayLabTime;
    @Column(name = "absence_times")
    private int absenceTimes;

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

    public double getStayLabTime() {
        return stayLabTime;
    }

    public void setStayLabTime(double stayLabTime) {
        this.stayLabTime = stayLabTime;
    }

    public int getAbsenceTimes() {
        return absenceTimes;
    }

    public void setAbsenceTimes(int absenceTimes) {
        this.absenceTimes = absenceTimes;
    }
}

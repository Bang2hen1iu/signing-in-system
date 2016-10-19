package com.xuemiao.model.pdm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

/**
 * Created by dzj on 10/1/2016.
 */
@Entity
@Table(name = "statistics")
public class StatisticsEntity {
    @Id
    @Column(name = "sign_in_info_id")
    private Long signInInfoId;
    @Column(name = "student_id")
    private Long studentId;
    @Column(name = "oper_date")
    private Date operDate;
    @Column(name = "stay_lab_time")
    private double stayLabTime;
    @Column(name = "absence_times")
    private int absenceTimes;

    public Long getSignInInfoId() {
        return signInInfoId;
    }

    public void setSignInInfoId(Long signInInfoId) {
        this.signInInfoId = signInInfoId;
    }

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

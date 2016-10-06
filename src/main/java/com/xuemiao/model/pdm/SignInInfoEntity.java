package com.xuemiao.model.pdm;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by dzj on 9/30/2016.
 */
@Entity
@IdClass(value = StudentIdAndOperDateKey.class)
@Table(name = "sign_in_info")
public class SignInInfoEntity implements Serializable {
    @Id
    private Long studentId;
    @Id
    private Date operDate;
    @Column(name = "start_morning")
    private Timestamp startMorning;
    @Column(name = "end_morning")
    private Timestamp endMorning;
    @Column(name = "start_afternoon")
    private Timestamp startAfternoon;
    @Column(name = "end_afternoon")
    private Timestamp endAfternoon;
    @Column(name = "start_night")
    private Timestamp startNight;
    @Column(name = "end_night")
    private Timestamp endNight;
    @Column(name = "current_day_courses")
    private String currentDayCourses;
    @Column(name = "signature_img_name")
    String signatureImgName;

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

    public Timestamp getStartMorning() {
        return startMorning;
    }

    public void setStartMorning(Timestamp startMorning) {
        this.startMorning = startMorning;
    }

    public Timestamp getEndMorning() {
        return endMorning;
    }

    public void setEndMorning(Timestamp endMorning) {
        this.endMorning = endMorning;
    }

    public Timestamp getStartAfternoon() {
        return startAfternoon;
    }

    public void setStartAfternoon(Timestamp startAfternoon) {
        this.startAfternoon = startAfternoon;
    }

    public Timestamp getEndAfternoon() {
        return endAfternoon;
    }

    public void setEndAfternoon(Timestamp endAfternoon) {
        this.endAfternoon = endAfternoon;
    }

    public Timestamp getStartNight() {
        return startNight;
    }

    public void setStartNight(Timestamp startNight) {
        this.startNight = startNight;
    }

    public Timestamp getEndNight() {
        return endNight;
    }

    public void setEndNight(Timestamp endNight) {
        this.endNight = endNight;
    }

    public String getCurrentDayCourses() {
        return currentDayCourses;
    }

    public void setCurrentDayCourses(String currentDayCourses) {
        this.currentDayCourses = currentDayCourses;
    }

    public String getSignatureImgName() {
        return signatureImgName;
    }

    public void setSignatureImgName(String signatureImgName) {
        this.signatureImgName = signatureImgName;
    }
}

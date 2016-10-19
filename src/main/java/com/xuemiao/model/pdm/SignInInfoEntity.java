package com.xuemiao.model.pdm;

import com.xuemiao.model.pdm.primaryKey.StudentIdAndOperDateKey;

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
    @Column(name = "start_morning_signature_img_name")
    private String startMorningSignatureImgName;
    @Column(name = "end_morning_signature_img_name")
    private String endMorningSignatureImgName;
    @Column(name = "start_afternoon_signature_img_name")
    private String startAfternoonSignatureImgName;
    @Column(name = "end_afternoon_signature_img_name")
    private String endAfternoonSignatureImgName;
    @Column(name = "start_night_signature_img_name")
    private String startNightSignatureImgName;
    @Column(name = "end_night_signature_img_name")
    private String endNightSignatureImgName;

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

    public String getStartMorningSignatureImgName() {
        return startMorningSignatureImgName;
    }

    public void setStartMorningSignatureImgName(String startMorningSignatureImgName) {
        this.startMorningSignatureImgName = startMorningSignatureImgName;
    }

    public String getEndMorningSignatureImgName() {
        return endMorningSignatureImgName;
    }

    public void setEndMorningSignatureImgName(String endMorningSignatureImgName) {
        this.endMorningSignatureImgName = endMorningSignatureImgName;
    }

    public String getStartAfternoonSignatureImgName() {
        return startAfternoonSignatureImgName;
    }

    public void setStartAfternoonSignatureImgName(String startAfternoonSignatureImgName) {
        this.startAfternoonSignatureImgName = startAfternoonSignatureImgName;
    }

    public String getEndAfternoonSignatureImgName() {
        return endAfternoonSignatureImgName;
    }

    public void setEndAfternoonSignatureImgName(String endAfternoonSignatureImgName) {
        this.endAfternoonSignatureImgName = endAfternoonSignatureImgName;
    }

    public String getStartNightSignatureImgName() {
        return startNightSignatureImgName;
    }

    public void setStartNightSignatureImgName(String startNightSignatureImgName) {
        this.startNightSignatureImgName = startNightSignatureImgName;
    }

    public String getEndNightSignatureImgName() {
        return endNightSignatureImgName;
    }

    public void setEndNightSignatureImgName(String endNightSignatureImgName) {
        this.endNightSignatureImgName = endNightSignatureImgName;
    }
}

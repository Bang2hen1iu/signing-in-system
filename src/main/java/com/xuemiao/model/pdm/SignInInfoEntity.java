package com.xuemiao.model.pdm;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

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
    private DateTime operDate;
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
}

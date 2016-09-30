package com.xuemiao.model.pdm;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by dzj on 9/30/2016.
 */
@Entity
@IdClass(value = StudentAndCourseNameKey.class)
@Table(name = "course")
public class CourseEntity implements Serializable {
    @Id
    private Long studentId;
    @Id
    private Date courseName;
    @Column(name = "start_week")
    private int startWeek;
    @Column(name = "end_week")
    private int endWeek;
    @Column(name = "weekday")
    private int weekDay;
    @Column(name = "start_section")
    private int startSection;
    @Column(name = "end_section")
    private int endSection;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Date getCourseName() {
        return courseName;
    }

    public void setCourseName(Date courseName) {
        this.courseName = courseName;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public int getStartSection() {
        return startSection;
    }

    public void setStartSection(int startSection) {
        this.startSection = startSection;
    }

    public int getEndSection() {
        return endSection;
    }

    public void setEndSection(int endSection) {
        this.endSection = endSection;
    }
}

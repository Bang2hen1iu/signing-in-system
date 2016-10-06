package com.xuemiao.model.pdm;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by dzj on 10/4/2016.
 */
@Embeddable
public class CoursePerWeekPKey implements Serializable {
    @Column(name = "student_id")
    private Long studentId;
    @Column(name = "course_name")
    private String courseName;
    @Column(name = "weekday")
    private int weekday;


    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }
}

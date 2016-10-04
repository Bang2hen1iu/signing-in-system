package com.xuemiao.model.pdm;

import javax.persistence.*;
import java.io.Serializable;

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
    private String courseName;
    @Column(name = "start_week")
    private int startWeek;
    @Column(name = "end_week")
    private int endWeek;

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
}

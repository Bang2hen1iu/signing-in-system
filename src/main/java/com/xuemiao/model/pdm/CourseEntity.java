package com.xuemiao.model.pdm;

import javax.persistence.*;

/**
 * Created by dzj on 9/30/2016.
 */
@Entity
@Table(name = "course")
public class CourseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;
    @Column(name = "student_id")
    private Long studentId;
    @Column(name = "course_name")
    private String courseName;
    @Column(name = "start_week")
    private int startWeek;
    @Column(name = "end_week")
    private int endWeek;
    @Column(name = "semester_id")
    private Long semesterId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }
}

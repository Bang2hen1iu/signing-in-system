package com.xuemiao.api.Json;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dzj on 10/4/2016.
 */
public class CoursesInfoJson implements Serializable {
    private Long id;
    private Long studentId;
    private String courseName;
    private int startWeek;
    private int endWeek;
    private List<CoursePerWeekJson> coursePerWeekJsonList;

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

    public List<CoursePerWeekJson> getCoursePerWeekJsonList() {
        return coursePerWeekJsonList;
    }

    public void setCoursePerWeekJsonList(List<CoursePerWeekJson> coursePerWeekJsonList) {
        this.coursePerWeekJsonList = coursePerWeekJsonList;
    }
}

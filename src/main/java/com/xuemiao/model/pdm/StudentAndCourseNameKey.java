package com.xuemiao.model.pdm;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by dzj on 9/30/2016.
 */
@Embeddable
public class StudentAndCourseNameKey implements Serializable{
    @Column(name = "student_id")
    private String studentId;
    @Column(name = "course_name")
    private Date courseName;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Date getCourseName() {
        return courseName;
    }

    public void setCourseName(Date courseName) {
        this.courseName = courseName;
    }
}

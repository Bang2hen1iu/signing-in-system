package com.xuemiao.model.pdm;

import com.xuemiao.model.pdm.primaryKey.CoursePerWeekPKey;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by dzj on 10/4/2016.
 */
@Entity
@IdClass(value = CoursePerWeekPKey.class)
@Table(name = "course_per_week")
public class CoursePerWeekEntity implements Serializable {
    @Id
    private Long courseId;
    @Id
    private int weekday;
    @Column(name = "start_section")
    private int startSection;
    @Column(name = "end_section")
    private int endSection;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
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

package com.xuemiao.model.pdm.primaryKey;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by dzj on 10/4/2016.
 */
@Embeddable
public class CoursePerWeekPKey implements Serializable {
    @Column(name = "course_id")
    private Long courseId;
    @Column(name = "weekday")
    private int weekday;

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
}

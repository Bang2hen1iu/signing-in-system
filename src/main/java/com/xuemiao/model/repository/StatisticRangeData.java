package com.xuemiao.model.repository;

/**
 * Created by dzj on 10/2/2016.
 */
public class StatisticRangeData {
    private Long studentId;
    private float stayLabTime;
    private int absenceTimes;

    public StatisticRangeData(Long studentId, float stayLabTime, int absenceTimes) {
        this.studentId = studentId;
        this.stayLabTime = stayLabTime;
        this.absenceTimes = absenceTimes;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public float getStayLabTime() {
        return stayLabTime;
    }

    public void setStayLabTime(float stayLabTime) {
        this.stayLabTime = stayLabTime;
    }

    public int getAbsenceTimes() {
        return absenceTimes;
    }

    public void setAbsenceTimes(int absenceTimes) {
        this.absenceTimes = absenceTimes;
    }
}

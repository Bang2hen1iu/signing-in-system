package com.xuemiao.api.Json;

import java.io.Serializable;

/**
 * Created by dzj on 10/4/2016.
 */
public class CoursePerWeekJson implements Serializable{
    private int weekday;
    private int startSection;
    private int endSection;

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

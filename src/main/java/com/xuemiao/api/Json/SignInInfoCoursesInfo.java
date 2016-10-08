package com.xuemiao.api.Json;

import java.io.Serializable;

/**
 * Created by dzj on 10/8/2016.
 */
public class SignInInfoCoursesInfo implements Serializable{
    private String courseName;
    private int startSection;
    private int endSection;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

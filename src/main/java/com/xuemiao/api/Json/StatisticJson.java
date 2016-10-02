package com.xuemiao.api.Json;

/**
 * Created by dzj on 10/2/2016.
 */
public class StatisticJson {
    private Long id;
    private String name;
    private double stayLabTime;
    private Long absenceTimes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStayLabTime() {
        return stayLabTime;
    }

    public void setStayLabTime(float stayLabTime) {
        this.stayLabTime = stayLabTime;
    }

    public void setStayLabTime(double stayLabTime) {
        this.stayLabTime = stayLabTime;
    }

    public Long getAbsenceTimes() {
        return absenceTimes;
    }

    public void setAbsenceTimes(Long absenceTimes) {
        this.absenceTimes = absenceTimes;
    }
}

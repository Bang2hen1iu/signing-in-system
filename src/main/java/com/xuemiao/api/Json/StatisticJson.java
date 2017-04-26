package com.xuemiao.api.Json;

/**
 * Created by dzj on 10/2/2016.
 */
public class StatisticJson {
    protected String name;
    protected Double stayLabTime;
    protected int absenceTimes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getStayLabTime() {
        return stayLabTime;
    }

    public void setStayLabTime(Double stayLabTime) {
        this.stayLabTime = stayLabTime;
    }

    public int getAbsenceTimes() {
        return absenceTimes;
    }

    public void setAbsenceTimes(int absenceTimes) {
        this.absenceTimes = absenceTimes;
    }
}

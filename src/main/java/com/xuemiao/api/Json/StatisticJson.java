package com.xuemiao.api.Json;

/**
 * Created by dzj on 10/2/2016.
 */
public class StatisticJson {
    private String name;
    private String stayLabTime;
    private int absenceTimes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStayLabTime() {
        return stayLabTime;
    }

    public void setStayLabTime(String stayLabTime) {
        this.stayLabTime = stayLabTime;
    }

    public int getAbsenceTimes() {
        return absenceTimes;
    }

    public void setAbsenceTimes(int absenceTimes) {
        this.absenceTimes = absenceTimes;
    }
}

package com.xuemiao.api.Json;

/**
 * Created by dzj on 10/2/2016.
 */
public class StatisticJson {
    public String name;
    public double stayLabTime;
    public double absenceTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStayLabTime() {
        return stayLabTime;
    }

    public void setStayLabTime(double stayLabTime) {
        this.stayLabTime = stayLabTime;
    }

    public double getAbsenceTime() {
        return absenceTime;
    }

    public void setAbsenceTime(double absenceTime) {
        this.absenceTime = absenceTime;
    }
}

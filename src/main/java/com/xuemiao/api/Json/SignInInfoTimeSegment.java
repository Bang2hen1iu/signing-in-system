package com.xuemiao.api.Json;

import java.io.Serializable;

/**
 * Created by root on 16-10-23.
 */
public class SignInInfoTimeSegment implements Serializable, Comparable<SignInInfoTimeSegment> {
    private String startTime;
    private String endTime;
    private String width;
    private int type;
    private String extra;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public int compareTo(SignInInfoTimeSegment o) {
        return this.startTime.compareTo(o.startTime);
    }
}

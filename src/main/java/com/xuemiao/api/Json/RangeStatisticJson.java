package com.xuemiao.api.Json;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zijun on 17-4-26.
 */
public class RangeStatisticJson implements Serializable{
    private List<StatisticJson> statisticJsons;
    private String downloadCode;

    public List<StatisticJson> getStatisticJsons() {
        return statisticJsons;
    }

    public void setStatisticJsons(List<StatisticJson> statisticJsons) {
        this.statisticJsons = statisticJsons;
    }

    public String getDownloadCode() {
        return downloadCode;
    }

    public void setDownloadCode(String downloadCode) {
        this.downloadCode = downloadCode;
    }
}

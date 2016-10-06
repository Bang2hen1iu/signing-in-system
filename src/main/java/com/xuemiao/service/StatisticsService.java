package com.xuemiao.service;

import com.xuemiao.api.Json.StatisticJson;
import com.xuemiao.model.repository.StudentRepository;
import com.xuemiao.utils.PrecisionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dzj on 10/4/2016.
 */
@Component
public class StatisticsService {
    @Autowired
    StudentRepository studentRepository;

    public List<StatisticJson> object2Json(List<Object[]> statisticList) {
        List<StatisticJson> statisticJsonList = new ArrayList<>();
        for (Object[] statistic : statisticList) {
            StatisticJson statisticJson = new StatisticJson();
            statisticJson.setId((Long) statistic[0]);
            statisticJson.setStayLabTime(PrecisionUtils.transferToSecondDecimal((double) statistic[1]));
            statisticJson.setAbsenceTimes((Long) statistic[2]);
            statisticJson.setName(studentRepository.findOne((Long) statistic[0]).getName());
            statisticJsonList.add(statisticJson);
        }
        return statisticJsonList;
    }
}

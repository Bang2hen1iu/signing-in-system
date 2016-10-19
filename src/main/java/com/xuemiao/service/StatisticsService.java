package com.xuemiao.service;

import com.xuemiao.api.Json.StatisticJson;
import com.xuemiao.model.pdm.StatisticsEntity;
import com.xuemiao.model.repository.StatisticsRepository;
import com.xuemiao.model.repository.StudentRepository;
import com.xuemiao.utils.DateUtils;
import com.xuemiao.utils.PrecisionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dzj on 10/4/2016.
 */
@Component
public class StatisticsService {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    StatisticsRepository statisticsRepository;

    public List<StatisticJson> getRangeStatistics(Date startDate, Date endDate) {
        List<Object[]> statisticRangeDataList = statisticsRepository.getRangeStatistics(startDate, endDate);
        return statisticsListToJsons(statisticRangeDataList);
    }

    public List<StatisticJson> getStatisticsOfThisMonth() {
        DateTime now = DateTime.now();
        DateTime dateTime = new DateTime(now.getYear(), now.getMonthOfYear(), 1, 0, 0, 0, 0);
        List<Object[]> statisticList = statisticsRepository.getStatisticsByStartDate(new Date(dateTime.getMillis()));
        return statisticsListToJsons(statisticList);
    }

    public List<StatisticsEntity> getStatisticsByDate(Date date) {
        List<StatisticsEntity> statisticsEntities = statisticsRepository.findByOperDate(date);
        return statisticsEntities;
    }

    public Date getLatestDate() {
        return statisticsRepository.getLatestStatisticsDate();
    }

    public List<String> getAllDate() {
        return DateUtils.DateList2DateStringList(statisticsRepository.getAllStatisticsDate());
    }

    private List<StatisticJson> statisticsListToJsons(List<Object[]> statisticList) {
        List<StatisticJson> statisticJsonList = new ArrayList<>();
        for (Object[] statistic : statisticList) {
            statisticJsonList.add(wrapStatisticIntoJson(statistic));
        }
        return statisticJsonList;
    }

    private StatisticJson wrapStatisticIntoJson(Object[] statistic) {
        StatisticJson statisticJson = new StatisticJson();
        statisticJson.setId((Long) statistic[0]);
        statisticJson.setStayLabTime(PrecisionUtils.transferToSecondDecimal((double) statistic[1]));
        statisticJson.setAbsenceTimes((Long) statistic[2]);
        statisticJson.setName(studentRepository.findOne((Long) statistic[0]).getName());
        return statisticJson;
    }

}

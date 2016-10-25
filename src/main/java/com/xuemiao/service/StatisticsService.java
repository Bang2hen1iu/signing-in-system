package com.xuemiao.service;

import com.xuemiao.api.Json.StatisticJson;
import com.xuemiao.model.pdm.SignInInfoV2Entity;
import com.xuemiao.model.repository.AbsenceRepository;
import com.xuemiao.model.repository.SignInInfoV2Repository;
import com.xuemiao.model.repository.StatisticsRepositoryService;
import com.xuemiao.model.repository.StudentRepository;
import com.xuemiao.utils.DateUtils;
import com.xuemiao.utils.PrecisionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dzj on 10/4/2016.
 */
@Service
public class StatisticsService {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    StatisticsRepositoryService statisticsRepositoryService;

    public List<StatisticJson> getRangeStatistics(Date startDate, Date endDate) {
        return statisticsRepositoryService.getRangeStatistics(startDate,endDate);
    }

    public List<StatisticJson> getStatisticsOfThisMonth() {
        DateTime now = DateTime.now();
        DateTime dateTime = new DateTime(now.getYear(), now.getMonthOfYear(), 1, 0, 0, 0, 0);
        return statisticsRepositoryService.getStatisticsByStartDateUpToNow(new Date(dateTime.getMillis()));
    }

}

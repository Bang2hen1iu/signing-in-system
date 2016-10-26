package com.xuemiao.service;

import com.xuemiao.model.pdm.*;
import com.xuemiao.model.pdm.primaryKey.StudentIdAndOperDateKey;
import com.xuemiao.model.repository.*;
import com.xuemiao.utils.DateUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by dzj on 10/1/2016.
 */
@Component
@Scope("singleton")
public class ScheduleTaskService {
    private final static int PERIOD_IN_SECONDS = 24*3600;
    private final Logger LOGGER = LoggerFactory.getLogger(ScheduleTaskService.class);
    ScheduledExecutorService scheduledExecutorService = null;
    @Autowired
    StudentRepository studentRepository;
    @Value("${schedule.task_execute_time}")
    int scheduleStartHour;
    @Autowired
    SignInInfoV2Repository signInInfoV2Repository;

    public synchronized void startRefreshSignInfoTable() {
        if (scheduledExecutorService != null) {
            LOGGER.warn("Refresh scheduler has already start.");
        }
        DateTime startTime = DateTime.now();
        int hourNow = startTime.getHourOfDay();
        if (hourNow >= scheduleStartHour) {
            startTime = startTime.minusDays(-1);
        }
        startTime = startTime.minusHours(hourNow - scheduleStartHour);
        int timeGapToStartInSecond = DateUtils.getTimeGapInSecond(DateTime.now(), startTime);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            List<StudentEntity> studentEntities = studentRepository.findAll();
            DateTime currentDate = DateTime.now();
            for (StudentEntity studentEntity : studentEntities) {
                SignInInfoV2Entity signInInfoV2Entity = new SignInInfoV2Entity();
                signInInfoV2Entity.setStudentId(studentEntity.getStudentId());
                signInInfoV2Entity.setOperDate(new Date(currentDate.getMillis()));
                signInInfoV2Repository.save(signInInfoV2Entity);
            }
        }, timeGapToStartInSecond, PERIOD_IN_SECONDS, TimeUnit.SECONDS);
    }

    @PreDestroy
    public synchronized void shutdownScheduler() {
        if (scheduledExecutorService == null) {
            return;
        }
        scheduledExecutorService.shutdown();
        try {
            scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            scheduledExecutorService = null;
        }
    }
}

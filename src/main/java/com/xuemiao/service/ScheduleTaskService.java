package com.xuemiao.service;

import com.xuemiao.model.pdm.PlanRecordEntity;
import com.xuemiao.model.pdm.SignInInfoV2Entity;
import com.xuemiao.model.pdm.StudentEntity;
import com.xuemiao.model.pdm.WeekPlanEntity;
import com.xuemiao.model.repository.PlanRecordRepository;
import com.xuemiao.model.repository.SignInInfoV2Repository;
import com.xuemiao.model.repository.StudentRepository;
import com.xuemiao.model.repository.WeekPlanRepository;
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
import java.sql.Timestamp;
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
    private final Logger LOGGER = LoggerFactory.getLogger(ScheduleTaskService.class);
    ScheduledExecutorService scheduledExecutorService = null;
    @Autowired
    StudentRepository studentRepository;
    @Value("${schedule.task_execute_time}")
    int scheduleStartHour;
    @Autowired
    SignInInfoV2Repository signInInfoV2Repository;
    @Autowired
    WeekPlanRepository weekPlanRepository;
    @Autowired
    PlanRecordRepository planRecordRepository;

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
                SignInInfoV2Entity signInInfoV2Entity = signInInfoV2Repository.findOneByStudentIdAndDate(
                        studentEntity.getStudentId(), new Date(currentDate.getMillis()));
                if (signInInfoV2Entity == null) {
                    signInInfoV2Entity = new SignInInfoV2Entity();
                    signInInfoV2Entity.setStudentId(studentEntity.getStudentId());
                    signInInfoV2Entity.setOperDate(new Date(currentDate.getMillis()));
                    signInInfoV2Repository.save(signInInfoV2Entity);
                }
            }
        }, timeGapToStartInSecond, 1, TimeUnit.DAYS);
    }

    private void injectWeekPlan() {
        WeekPlanEntity weekPlanEntity = new WeekPlanEntity();
        DateTime monday = DateTime.now();
        DateTime sunday = DateTime.now();
        sunday.plusDays(6);
        weekPlanEntity.setWeekName(monday.getYear() + "." + monday.getMonthOfYear() + "." + monday.getDayOfMonth()
                + "~" + sunday.getYear() + "." + sunday.getMonthOfYear() + "." + sunday.getDayOfMonth());
        weekPlanEntity.setCreateAt(new Timestamp(monday.getMillis()));
        weekPlanEntity = weekPlanRepository.saveAndFlush(weekPlanEntity);

        Long id = weekPlanEntity.getId();
        List<StudentEntity> studentEntities = studentRepository.findAll();
        for (StudentEntity s : studentEntities){
            PlanRecordEntity p = new PlanRecordEntity();
            p.setPlanId(id);
            p.setPlan("");
            p.setStudentId(s.getStudentId());
            planRecordRepository.save(p);
        }
    }

    public synchronized void startRefreshWeekPlan() {
        if (scheduledExecutorService != null) {
            LOGGER.warn("Refresh scheduler has already start.");
        }
        DateTime startTime = DateTime.now();

        Timestamp latestWeekPlanTime = weekPlanRepository.getLatestWeekPlan();
        if (latestWeekPlanTime == null){
            injectWeekPlan();
        }
        else {
            DateTime latest = new DateTime(latestWeekPlanTime.getTime());
            if (!((latest.getWeekyear() == startTime.getWeekyear()) &&
                    (latest.getWeekOfWeekyear() == startTime.getWeekOfWeekyear()))) {
                injectWeekPlan();
            }
        }

        startTime.plusDays(8 - startTime.getDayOfWeek());
        startTime.minusHours(startTime.getHourOfDay() - 1);

        int timeGapToStartInSecond = DateUtils.getTimeGapInSecond(DateTime.now(), startTime);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this::injectWeekPlan, timeGapToStartInSecond, 7, TimeUnit.DAYS);
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

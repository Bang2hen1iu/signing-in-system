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
    @Autowired
    CourseRepository courseRepository;
    @Value("${course.start_date}")
    String courseStartDateString;
    @Autowired
    StatisticsRepository statisticsRepository;
    @Autowired
    AbsenceRepository absenceRepository;
    @Value("${task_execute_time}")
    int startHour;
    @Autowired
    CoursePerWeekRepository coursePerWeekRepository;
    @Autowired
    SignInInfoV2Repository signInInfoV2Repository;
    @Autowired
    SignInInfoRecordRepository signInInfoRecordRepository;

    public synchronized void startRefreshSignInfoTable() {
        if (scheduledExecutorService != null) {
            LOGGER.warn("Refresh scheduler has already start.");
        }
        DateTime startTime = DateTime.now();
        int hourNow = startTime.getHourOfDay();
        if (hourNow >= startHour) {
            startTime = startTime.minusDays(-1);
        }
        startTime = startTime.minusHours(hourNow - startHour);
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

            DateTime previousDate = currentDate.minusDays(1);
            AbsenceEntity absenceEntity = null;
            for (StudentEntity studentEntity : studentEntities) {
                SignInInfoV2Entity signInInfoV2Entity = signInInfoV2Repository.findOneByStudentIdAndDate(
                        studentEntity.getStudentId(), new Date(previousDate.getMillis()));
                if (signInInfoV2Entity != null) {
                    StatisticsEntity statisticsEntity = new StatisticsEntity();
                    statisticsEntity.setSignInInfoId(signInInfoV2Entity.getId());
                    statisticsEntity.setStudentId(studentEntity.getStudentId());
                    statisticsEntity.setOperDate(new Date(previousDate.getMillis()));
                    absenceEntity = absenceRepository.findOne(signInInfoV2Entity.getId());
                    if (absenceEntity == null) {
                        statisticsEntity.setAbsenceTimes(0);
                    } else {
                        statisticsEntity.setAbsenceTimes(1);
                    }
                    List<SignInInfoRecordEntity> signInInfoRecordEntities = signInInfoRecordRepository.findBySignInInfoId(signInInfoV2Entity.getId());
                    Long stayLabTimeL = new Long("0");
                    for (SignInInfoRecordEntity signInInfoRecordEntity : signInInfoRecordEntities) {
                        if (signInInfoRecordEntity.getEndTime() != null) {
                            stayLabTimeL += (signInInfoRecordEntity.getEndTime().getTime() - signInInfoRecordEntity.getStartTime().getTime());
                        }
                    }
                    double stayLabTimeD = stayLabTimeL.doubleValue();
                    stayLabTimeD /= 3600000;
                    statisticsEntity.setStayLabTime(stayLabTimeD);
                    statisticsRepository.save(statisticsEntity);
                }
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

package com.xuemiao.service;

import com.xuemiao.model.pdm.*;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private final static int PERIOD_IN_SECONDS = 86400;
    ScheduledExecutorService scheduledExecutorService = null;
    private final Logger LOGGER = LoggerFactory.getLogger(ScheduleTaskService.class);
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    SignInInfoRepository signInInfoRepository;
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

    public synchronized void startRefreshSignInfoTable(){
        if (scheduledExecutorService != null) {
            LOGGER.warn("Refresh scheduler has already start.");
        }
        DateTime startTime = DateTime.now();
        int hourNow = startTime.getHourOfDay();
        if(hourNow>startHour){
            startTime = startTime.minusDays(-1);
        }
        startTime = startTime.minusHours(hourNow-startHour);
        int timeGapToStartInSecond = DateUtils.getTimeGapInSecond(DateTime.now(),startTime);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            List<StudentEntity> studentEntities = studentRepository.findAll();
            SignInInfoEntity signInInfoEntity = new SignInInfoEntity();
            String currentDayCourseString = null;
            List<CourseEntity> courseEntities = null;
            DateTime startDate = null;
            startDate = DateTime.parse(courseStartDateString);
            DateTime currentDate = DateTime.now();
            int currentWeek = DateUtils.getCurrentWeek(startDate, currentDate);
            int currentWeekDay = DateUtils.getCurrentWeekDay(startDate, currentDate);
            CoursePerWeekEntity coursePerWeekEntity;
            StudentAndCourseNameKey studentAndCourseNameKey = new StudentAndCourseNameKey();
            for(StudentEntity studentEntity : studentEntities){
                signInInfoEntity.setStudentId(studentEntity.getStudentId());
                signInInfoEntity.setOperDate(new Date(currentDate.getMillis()));
                currentDayCourseString  = "";
                courseEntities = courseRepository.getCoursesByStudentAndWeek(studentEntity.getStudentId(), currentWeek);
                for(CourseEntity courseEntity : courseEntities){
                    coursePerWeekEntity = coursePerWeekRepository.findOneByIdAndNameAndWeekday(
                            courseEntity.getStudentId(), courseEntity.getCourseName(),currentWeekDay);
                    currentDayCourseString += courseEntity.getCourseName() + "第" + coursePerWeekEntity.getStartSection() +
                            "~" + coursePerWeekEntity.getEndSection() + "节；";
                }
                signInInfoEntity.setCurrentDayCourses(currentDayCourseString);

                signInInfoRepository.save(signInInfoEntity);
            }

            DateTime previousDate = currentDate.minusDays(1);
            StudentIdAndOperDateKey studentIdAndOperDateKey = new StudentIdAndOperDateKey();
            StatisticsEntity statisticsEntity = new StatisticsEntity();
            AbsenceEntity absenceEntity = null;
            for(StudentEntity studentEntity : studentEntities) {
                studentIdAndOperDateKey.setStudentId(studentEntity.getStudentId());
                studentIdAndOperDateKey.setOperDate(new Date(previousDate.getMillis()));
                signInInfoEntity = signInInfoRepository.findOne(studentIdAndOperDateKey);
                if (signInInfoEntity != null) {
                    statisticsEntity.setStudentId(studentEntity.getStudentId());
                    statisticsEntity.setOperDate(new Date(previousDate.getMillis()));
                    absenceEntity = absenceRepository.findOne(studentIdAndOperDateKey);
                    if (absenceEntity == null) {
                        statisticsEntity.setAbsenceTimes(0);
                    } else {
                        statisticsEntity.setAbsenceTimes(1);
                    }
                    Long stayLabTimeL = new Long("0");
                    if (signInInfoEntity.getEndMorning() != null && signInInfoEntity.getStartMorning() != null) {
                        stayLabTimeL += (signInInfoEntity.getEndMorning().getTime() - signInInfoEntity.getStartMorning().getTime());
                    }
                    if (signInInfoEntity.getEndAfternoon() != null && signInInfoEntity.getStartAfternoon() != null) {
                        stayLabTimeL += (signInInfoEntity.getEndAfternoon().getTime() - signInInfoEntity.getStartAfternoon().getTime());
                    }
                    if (signInInfoEntity.getEndNight() != null && signInInfoEntity.getStartNight() != null) {
                        stayLabTimeL += (signInInfoEntity.getEndNight().getTime() - signInInfoEntity.getStartNight().getTime());
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

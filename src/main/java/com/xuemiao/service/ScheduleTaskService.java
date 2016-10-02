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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private final static int PERIOD_IN_SECONDS = 5;
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

    public synchronized void startRefreshSignInfoTable(){
        if (scheduledExecutorService != null) {
            LOGGER.warn("Refresh scheduler has already start.");
        }
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
            for(StudentEntity studentEntity : studentEntities){
                signInInfoEntity.setStudentId(studentEntity.getStudentId());
                signInInfoEntity.setOperDate(currentDate);
                currentDayCourseString  = "";
                courseEntities = courseRepository.getCoursesByStudentAndWeek(
                        studentEntity.getStudentId(), currentWeek, currentWeekDay);
                for(CourseEntity courseEntity : courseEntities){
                    currentDayCourseString += courseEntity.getCourseName() + "第" + courseEntity.getStartSection() +
                            "~" + courseEntity.getEndSection() + "节；";
                }
                signInInfoEntity.setCurrentDayCourses(currentDayCourseString);
            }

            DateTime previousDate = currentDate.minusDays(1);
            StudentIdAndOperDateKey studentIdAndOperDateKey = new StudentIdAndOperDateKey();
            StatisticsEntity statisticsEntity = new StatisticsEntity();
            AbsenceEntity absenceEntity = null;
            for(StudentEntity studentEntity : studentEntities){
                studentIdAndOperDateKey.setStudentId(studentEntity.getStudentId());
                studentIdAndOperDateKey.setOperDate(previousDate);
                signInInfoEntity = signInInfoRepository.findOne(studentIdAndOperDateKey);
                statisticsEntity.setStudentId(studentEntity.getStudentId());
                statisticsEntity.setOperDate(previousDate);

                absenceEntity = absenceRepository.findOne(studentIdAndOperDateKey);
                if(absenceEntity==null){
                    statisticsEntity.setAbsenceTimes(0);
                }
                else {
                    statisticsEntity.setAbsenceTimes(1);
                }
                Long stayLabTime = new Long("0");
                if(signInInfoEntity.getEndMorning()!=null&&signInInfoEntity.getStartMorning()!=null){
                    stayLabTime += signInInfoEntity.getEndMorning().getTime() - signInInfoEntity.getStartMorning().getTime();
                }
                if(signInInfoEntity.getEndAfternoon()!=null&&signInInfoEntity.getStartAfternoon()!=null){
                    stayLabTime += signInInfoEntity.getEndAfternoon().getTime() - signInInfoEntity.getStartAfternoon().getTime();
                }
                if(signInInfoEntity.getEndNight()!=null&&signInInfoEntity.getStartNight()!=null){
                    stayLabTime += signInInfoEntity.getEndNight().getTime() - signInInfoEntity.getStartNight().getTime();
                }
                stayLabTime /= 3600000;
                statisticsEntity.setStayLabTime(stayLabTime);
                statisticsRepository.save(statisticsEntity);
            }
        }, 0, PERIOD_IN_SECONDS, TimeUnit.SECONDS);
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

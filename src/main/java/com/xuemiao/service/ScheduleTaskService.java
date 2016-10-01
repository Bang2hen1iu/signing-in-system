package com.xuemiao.service;

import com.xuemiao.model.pdm.CourseEntity;
import com.xuemiao.model.pdm.SignInInfoEntity;
import com.xuemiao.model.pdm.StudentEntity;
import com.xuemiao.model.repository.CourseRepository;
import com.xuemiao.model.repository.SignInInfoRepository;
import com.xuemiao.model.repository.StudentRepository;
import com.xuemiao.utils.DateUtils;
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

    public synchronized void startRefreshSignInfoTable(){
        if (scheduledExecutorService != null) {
            LOGGER.warn("Refresh scheduler has already start.");
        }
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            List<StudentEntity> studentEntities = studentRepository.findAll();
            Date date = new Date();
            SignInInfoEntity signInInfoEntity = new SignInInfoEntity();
            String currentDayCourseString = null;
            List<CourseEntity> courseEntities = null;
            Date startDate = null;
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(courseStartDateString);
            }catch (ParseException e){
                LOGGER.error("Parse current date from string failed!");
            }
            Date currentDate = new Date();
            int currentWeek = DateUtils.getCurrentWeek(startDate, currentDate);
            int currentWeekDay = DateUtils.getCurrentWeekDay(startDate, currentDate);
            for(StudentEntity studentEntity : studentEntities){
                signInInfoEntity.setStudentId(studentEntity.getStudentId());
                signInInfoEntity.setOperDate(date);
                currentDayCourseString  = "";
                courseEntities = courseRepository.getCoursesByStudentAndWeek(
                        studentEntity.getStudentId(), currentWeek, currentWeekDay);
                for(CourseEntity courseEntity : courseEntities){
                    currentDayCourseString += courseEntity.getCourseName() + "第" + courseEntity.getStartSection() +
                            "~" + courseEntity.getEndSection() + "节；";
                }
                signInInfoEntity.setCurrentDayCourses(currentDayCourseString);
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

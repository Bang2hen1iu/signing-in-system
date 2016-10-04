package com.xuemiao.api;

import com.xuemiao.model.pdm.*;
import com.xuemiao.model.repository.*;
import com.xuemiao.utils.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dzj on 10/4/2016.
 */
@Component
@Path("/wa")
public class testDataInjectionApi {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    SignInInfoRepository signInInfoRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    CoursePerWeekRepository coursePerWeekRepository;
    @Autowired
    @Value("${course.start_date}")
    String courseStartDateString;
    @Autowired
    AbsenceRepository absenceRepository;
    @Autowired
    StatisticsRepository statisticsRepository;

    @POST
    @Path("/student")
    public Response injectStudent(){
        List<String> nameList = new ArrayList<>();
        nameList.add("邓梓君");
        nameList.add("雨天飞");
        nameList.add("李可可");
        nameList.add("谢敏珊");
        nameList.add("肖永杰");

        for (String name:nameList){
            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setName(name);
            studentRepository.save(studentEntity);
        }
        return Response.ok().build();
    }

    @POST
    @Path("/course")
    public Response injectCourse(){
        String courseName = "计算机视觉";
        List<StudentEntity> studentEntities = studentRepository.findAll();
        Random rand = new Random();
        for(StudentEntity studentEntity:studentEntities){
            CourseEntity courseEntity = new CourseEntity();
            courseEntity.setStudentId(studentEntity.getStudentId());
            courseEntity.setCourseName(courseName);
            courseEntity.setStartWeek(1 + rand.nextInt(10));
            courseEntity.setEndWeek(11 + rand.nextInt(10));
            courseRepository.save(courseEntity);

            CoursePerWeekEntity coursePerWeekEntity = new CoursePerWeekEntity();
            coursePerWeekEntity.setStudentId(studentEntity.getStudentId());
            coursePerWeekEntity.setCourseName(courseName);
            coursePerWeekEntity.setWeekday(1);
            coursePerWeekEntity.setStartSection(1);
            coursePerWeekEntity.setEndSection(2);
            coursePerWeekRepository.save(coursePerWeekEntity);

            coursePerWeekEntity.setWeekday(3);
            coursePerWeekEntity.setStartSection(3);
            coursePerWeekEntity.setEndSection(4);
            coursePerWeekRepository.save(coursePerWeekEntity);

            coursePerWeekEntity.setWeekday(5);
            coursePerWeekEntity.setStartSection(5);
            coursePerWeekEntity.setEndSection(8);
            coursePerWeekRepository.save(coursePerWeekEntity);
        }
        return Response.ok().build();
    }

    @GET
    @Path("/sign_in_info")
    public Response injectSignInInfo(){
        List<StudentEntity> studentEntities = studentRepository.findAll();
        SignInInfoEntity signInInfoEntity = new SignInInfoEntity();
        String currentDayCourseString = null;
        List<CourseEntity> courseEntities = null;
        DateTime startDate = null;
        startDate = DateTime.parse(courseStartDateString);
        DateTime currentDate = DateTime.now();
        int currentWeek = DateUtils.getCurrentWeek(startDate, currentDate);
        int currentWeekday = DateUtils.getCurrentWeekDay(startDate, currentDate);
        CoursePerWeekEntity coursePerWeekEntity;
        StudentAndCourseNameKey studentAndCourseNameKey = new StudentAndCourseNameKey();
        for(StudentEntity studentEntity : studentEntities){
            signInInfoEntity.setStudentId(studentEntity.getStudentId());
            signInInfoEntity.setOperDate(new Date(currentDate.getMillis()));
            currentDayCourseString  = "";
            courseEntities = courseRepository.getCoursesByStudentAndWeek(studentEntity.getStudentId(), currentWeek);
            for(CourseEntity courseEntity : courseEntities){
                System.out.println("AA"+courseEntity.getCourseName());
                coursePerWeekEntity = coursePerWeekRepository.findOneByIdAndNameAndWeekday(
                        courseEntity.getStudentId(), courseEntity.getCourseName(), currentWeekday);
                if(coursePerWeekEntity!=null){
                    currentDayCourseString += courseEntity.getCourseName() + "第" + coursePerWeekEntity.getStartSection() +
                            "~" + coursePerWeekEntity.getEndSection() + "节；";
                }
            }
            signInInfoEntity.setCurrentDayCourses(currentDayCourseString);

            signInInfoRepository.save(signInInfoEntity);
        }
        return Response.ok().build();
    }

    @POST
    @Path("/statistics")
    public Response injectStatistics(){
        List<StudentEntity> studentEntities = studentRepository.findAll();
        SignInInfoEntity signInInfoEntity;
        DateTime currentDate = DateTime.now();
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
        return Response.ok().build();
    }

}

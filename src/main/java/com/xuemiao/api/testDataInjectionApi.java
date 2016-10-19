package com.xuemiao.api;

import com.xuemiao.model.pdm.CourseEntity;
import com.xuemiao.model.pdm.CoursePerWeekEntity;
import com.xuemiao.model.pdm.SignInInfoV2Entity;
import com.xuemiao.model.pdm.StudentEntity;
import com.xuemiao.model.repository.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    @Autowired
    SignInInfoV2Repository signInInfoV2Repository;

    @POST
    @Path("/all")
    public Response injectAll() {
        injectStudent();
        injectCourse();
        injectSignInInfo();
        return Response.ok().build();
    }

    @POST
    @Path("/student")
    public Response injectStudent() {
        List<String> nameList = new ArrayList<>();
        nameList.add("邓梓君");
        nameList.add("雨天飞");
        nameList.add("李可可");
        nameList.add("谢敏珊");
        nameList.add("肖永杰");

        for (String name : nameList) {
            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setName(name);
            studentRepository.save(studentEntity);
        }
        return Response.ok().build();
    }

    @POST
    @Path("/course")
    public Response injectCourse() {
        String courseName = "计算机视觉";
        List<StudentEntity> studentEntities = studentRepository.findAll();
        Random rand = new Random();
        for (StudentEntity studentEntity : studentEntities) {
            CourseEntity courseEntity = new CourseEntity();
            courseEntity.setStudentId(studentEntity.getStudentId());
            courseEntity.setCourseName(courseName);
            courseEntity.setStartWeek(1 + rand.nextInt(10));
            courseEntity.setEndWeek(11 + rand.nextInt(10));
            courseRepository.save(courseEntity);

            CoursePerWeekEntity coursePerWeekEntity = new CoursePerWeekEntity();
            coursePerWeekEntity.setCourseId(courseEntity.getId());
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

    @POST
    @Path("/sign_in_info")
    public Response injectSignInInfo() {
        List<StudentEntity> studentEntities = studentRepository.findAll();
        DateTime currentDate = DateTime.now();

        for (StudentEntity studentEntity : studentEntities) {
            SignInInfoV2Entity signInInfoV2Entity = new SignInInfoV2Entity();
            signInInfoV2Entity.setStudentId(studentEntity.getStudentId());
            signInInfoV2Entity.setOperDate(new Date(currentDate.getMillis()));

            signInInfoV2Repository.save(signInInfoV2Entity);
        }
        return Response.ok().build();
    }

}

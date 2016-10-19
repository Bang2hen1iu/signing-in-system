package com.xuemiao.api;

import com.xuemiao.api.Json.*;
import com.xuemiao.exception.*;
import com.xuemiao.model.pdm.*;
import com.xuemiao.model.pdm.primaryKey.CoursePerWeekPKey;
import com.xuemiao.model.repository.*;
import com.xuemiao.service.AdminValidationService;
import com.xuemiao.service.CookieValidationService;
import com.xuemiao.service.StatisticsService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dzj on 9/30/2016.
 */
@Component
@Path("/admin_api")
public class AdminApi {
    @Value("${admin.cookie.token.path}")
    String cookiePath;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AdminValidationService adminValidationService;
    @Value("${admin.cookie.token.age}")
    int superAdminCookieAge;
    @Autowired
    CookieValidationService cookieValidationService;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    DutyStudentRepository dutyStudentRepository;
    @Autowired
    StatisticsRepository statisticsRepository;
    @Autowired
    CoursePerWeekRepository coursePerWeekRepository;
    @Autowired
    StatisticsService statisticsService;
    @Autowired
    AbsenceRepository absenceRepository;
    @Autowired
    SignInInfoRepository signInInfoRepository;

    @GET
    @Path("/admin/token_validation")
    public Response adminTokenValidation(@CookieParam("token") String tokenString)
            throws AdminTokenWrongException, TokenInvalidException {
        cookieValidationService.checkTokenCookie(tokenString, 2);
        return Response.ok().cookie(refreshCookie(tokenString)).build();
    }

    @POST
    @Path("/admin/validation")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adminValidation(IdPasswordJson idPasswordJson)
            throws IdNotExistException, PasswordErrorException {
        adminValidationService.testPassword(idPasswordJson.getId(), idPasswordJson.getPassword());
        return Response.ok().cookie(getCookie()).build();
    }

    private NewCookie getCookie() {
        return cookieValidationService.getTokenCookie(cookiePath, superAdminCookieAge);
    }

    private NewCookie refreshCookie(String tokenString) {
        return cookieValidationService.refreshCookie(tokenString, cookiePath, superAdminCookieAge);
    }

    @DELETE
    @Path("/admin/logout")
    public Response adminLogout(@CookieParam("token") String tokenString) {
        cookieValidationService.deleteCookieByToken(tokenString);
        return Response.ok().build();
    }

    @POST
    @Path("/students/addition")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStudent(StudentEntity studentEntity)
            throws StudentAdditionException {
        if (studentRepository.save(studentEntity) == null) {
            throw new StudentAdditionException();
        }
        SignInInfoEntity signInInfoEntity = new SignInInfoEntity();
        signInInfoEntity.setStudentId(studentEntity.getStudentId());
        signInInfoEntity.setOperDate(new Date(DateTime.now().getMillis()));
        signInInfoRepository.save(signInInfoEntity);
        return Response.ok().build();
    }

    @POST
    @Path("/students/registering")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerStudent(RegisterStudentJson registerStudentJson){
        StudentEntity studentEntity = studentRepository.findOne(registerStudentJson.getStudentId());
        //TODO save fingerprint
        studentRepository.save(studentEntity);
        return Response.ok().build();
    }

    @DELETE
    @Path("/student/deletion/{id}")
    public Response deleteStudent(@PathParam("id") Long id) {
        dutyStudentRepository.deleteByStudentId(id);
        List<CourseEntity> courseEntities = courseRepository.findByStudentId(id);
        for (CourseEntity courseEntity:courseEntities){
            coursePerWeekRepository.deleteByCourseId(courseEntity.getId());
        }
        courseRepository.deleteByStudentId(id);
        statisticsRepository.deleteByStudentId(id);
        absenceRepository.deleteByStudentId(id);
        signInInfoRepository.deleteByStudentId(id);

        studentRepository.delete(id);
        return Response.ok().build();
    }

    private void saveCoursePerWeekJson(Long courseId, CoursePerWeekJson coursePerWeekJson) {
        CoursePerWeekEntity coursePerWeekEntity = new CoursePerWeekEntity();
        coursePerWeekEntity.setCourseId(courseId);
        coursePerWeekEntity.setStartSection(coursePerWeekJson.getStartSection());
        coursePerWeekEntity.setEndSection(coursePerWeekJson.getEndSection());
        coursePerWeekEntity.setWeekday(coursePerWeekJson.getWeekday());
        coursePerWeekRepository.save(coursePerWeekEntity);
    }

    @POST
    @Path("/courses/addition")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCourse(CoursesInfoJson coursesInfoJson) {
        CourseEntity courseEntity = new CourseEntity();
        List<CoursePerWeekEntity> coursePerWeekEntities = new ArrayList<>();
        courseEntity.setStudentId(coursesInfoJson.getStudentId());
        courseEntity.setCourseName(coursesInfoJson.getCourseName());
        courseEntity.setStartWeek(coursesInfoJson.getStartWeek());
        courseEntity.setEndWeek(coursesInfoJson.getEndWeek());
        courseRepository.save(courseEntity);
        for (CoursePerWeekJson coursePerWeekJson : coursesInfoJson.getCoursePerWeekJsonList()) {
            if (coursePerWeekJson == null) {
                continue;
            }
            saveCoursePerWeekJson(coursesInfoJson.getId(), coursePerWeekJson);
        }
        return Response.ok().build();
    }

    @PUT
    @Path("/courses/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyCourse(CoursesInfoJson coursesInfoJson) {
        CourseEntity courseEntity = courseRepository.findOne(coursesInfoJson.getId());
        courseEntity.setStartWeek(coursesInfoJson.getStartWeek());
        courseEntity.setEndWeek(coursesInfoJson.getEndWeek());
        courseRepository.save(courseEntity);
        coursePerWeekRepository.deleteByCourseId(coursesInfoJson.getId());
        for (CoursePerWeekJson coursePerWeekJson : coursesInfoJson.getCoursePerWeekJsonList()) {
            saveCoursePerWeekJson(coursesInfoJson.getId(),coursePerWeekJson);
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/courses/deletion/{id}")
    public Response deleteCourse(@PathParam("id") Long id) {
        coursePerWeekRepository.deleteByCourseId(id);
        courseRepository.delete(id);
        return Response.ok().build();
    }

    @GET
    @Path("/duty_students")
    public Response getDutyStudents() {
        List<DutyStudentJson> dutyStudentJsonList = new ArrayList<>();
        List<DutyStudentEntity> dutyStudentEntities = dutyStudentRepository.findAll();
        for (DutyStudentEntity dutyStudentEntity : dutyStudentEntities) {
            DutyStudentJson dutyStudentJson = new DutyStudentJson();
            dutyStudentJson.setStudentId(dutyStudentEntity.getStudentId());
            dutyStudentJson.setStartDate(dutyStudentEntity.getStartDate());
            dutyStudentJson.setEndDate(dutyStudentEntity.getEndDate());
            dutyStudentJson.setName(studentRepository.findOne(dutyStudentEntity.getStudentId()).getName());
            dutyStudentJsonList.add(dutyStudentJson);
        }
        return Response.ok().entity(dutyStudentJsonList).build();
    }

    @POST
    @Path("/duty_student/addition")
    public Response addDutyStudent(DutyStudentEntity dutyStudentEntity) {
        dutyStudentRepository.save(dutyStudentEntity);
        return Response.ok().build();
    }

    @DELETE
    @Path("/duty_student/deletion")
    public Response deleteDutyStudent(@QueryParam("studentId") Long studentId) {
        dutyStudentRepository.deleteByStudentId(studentId);
        return Response.ok().build();
    }

    @GET
    @Path("/statistics/range_query")
    public Response rangeQueryStatistics(@QueryParam("startDate") Date startDate,
                                         @QueryParam("endDate") Date endDate) {
        System.out.println("AA:" + startDate);
        List<Object[]> statisticRangeDataList = statisticsRepository.getRangeStatistics(
                startDate, endDate);
        return Response.ok().entity(statisticsService.object2Json(statisticRangeDataList)).build();
    }

}

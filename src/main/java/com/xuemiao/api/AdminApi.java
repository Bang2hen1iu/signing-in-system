package com.xuemiao.api;

import com.xuemiao.api.Json.*;
import com.xuemiao.exception.AdminTokenWrongException;
import com.xuemiao.exception.IdNotExistException;
import com.xuemiao.exception.PasswordErrorException;
import com.xuemiao.exception.StudentAdditionException;
import com.xuemiao.model.pdm.*;
import com.xuemiao.model.repository.*;
import com.xuemiao.service.AdminValidationService;
import com.xuemiao.service.CookieValidationService;
import com.xuemiao.service.StatisticsService;
import com.xuemiao.utils.DateUtils;
import com.xuemiao.utils.PasswordUtils;
import com.xuemiao.utils.PrecisionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.Media;
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
    private final String cookiePath = "/api/admin_api";
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    SysAdminRepository sysAdminRepository;
    @Autowired
    AdminValidationService adminValidationService;
    @Value("${super-admin.cookie.token.age}")
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

    @GET
    @Path("/admin/token_validation")
    public Response adminTokenValidation(@CookieParam("token")String tokenString)
    throws AdminTokenWrongException{
        Response loginResponse = cookieValidationService.checkTokenCookie(tokenString, 2);
        if (loginResponse != null) {
            throw new AdminTokenWrongException();
        }
        return Response.ok().cookie(refreshCookie(tokenString)).build();
    }

    @POST
    @Path("/admin/validation")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adminValidation(IdPasswordJson idPasswordJson)
            throws IdNotExistException, PasswordErrorException {
        adminValidationService.testPassword(idPasswordJson.getId(), idPasswordJson.getPassword1(), 2);
        return Response.ok().cookie(getCookie(idPasswordJson.getId())).build();
    }

    private NewCookie getCookie(Long id) {
        return cookieValidationService.getTokenCookie(id, cookiePath, superAdminCookieAge);
    }

    private NewCookie refreshCookie(String tokenString) {
        return cookieValidationService.refreshCookie(tokenString, cookiePath, superAdminCookieAge);
    }

    @PUT
    @Path("/admin/password_update/{psw}")
    public Response adminPasswordUpdate(@PathParam("psw")String password)
            throws IdNotExistException, PasswordErrorException {
        adminValidationService.changePassword(password, 2);
        return Response.ok().build();
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
        return Response.ok().build();
    }

    @DELETE
    @Path("/student/deletion/{id}")
    public Response deleteStudent(@PathParam("id") Long id) {
        studentRepository.delete(id);
        return Response.ok().build();
    }

    private void saveCoursePerWeekJson(Long id, String name, CoursePerWeekJson coursePerWeekJson){
        CoursePerWeekEntity coursePerWeekEntity = new CoursePerWeekEntity();
        coursePerWeekEntity.setStudentId(id);
        coursePerWeekEntity.setCourseName(name);
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
        for (CoursePerWeekJson coursePerWeekJson : coursesInfoJson.getCoursePerWeekJsonList()){
            if(coursePerWeekJson==null){
                continue;
            }
            saveCoursePerWeekJson(coursesInfoJson.getStudentId(), coursesInfoJson.getCourseName(), coursePerWeekJson);
        }
        return Response.ok().build();
    }

    @PUT
    @Path("/courses/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyCourse(CoursesInfoJson coursesInfoJson){
        StudentAndCourseNameKey studentAndCourseNameKey = new StudentAndCourseNameKey();
        studentAndCourseNameKey.setStudentId(coursesInfoJson.getStudentId());
        studentAndCourseNameKey.setCourseName(coursesInfoJson.getCourseName());
        CourseEntity courseEntity = courseRepository.findOne(studentAndCourseNameKey);
        courseEntity.setStartWeek(coursesInfoJson.getStartWeek());
        courseEntity.setEndWeek(coursesInfoJson.getEndWeek());
        courseRepository.save(courseEntity);
        coursePerWeekRepository.deleteByStudentIdAndCourseName(coursesInfoJson.getStudentId(), coursesInfoJson.getCourseName());
        for (CoursePerWeekJson coursePerWeekJson : coursesInfoJson.getCoursePerWeekJsonList()){
            saveCoursePerWeekJson(coursesInfoJson.getStudentId(), coursesInfoJson.getCourseName(), coursePerWeekJson);
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/courses/deletion")
    public Response deleteCourse(@QueryParam("studentId")Long studentId,
                                 @QueryParam("courseName")String courseName){
        coursePerWeekRepository.deleteByStudentIdAndCourseName(studentId, courseName);
        StudentAndCourseNameKey studentAndCourseNameKey = new StudentAndCourseNameKey();
        studentAndCourseNameKey.setStudentId(studentId);
        studentAndCourseNameKey.setCourseName(courseName);
        courseRepository.delete(studentAndCourseNameKey);
        return Response.ok().build();
    }

    @GET
    @Path("/duty_students")
    public Response getDutyStudents() {
        List<DutyStudentJson> dutyStudentJsonList = new ArrayList<>();
        List<DutyStudentEntity> dutyStudentEntities = dutyStudentRepository.findAll();
        for (DutyStudentEntity dutyStudentEntity : dutyStudentEntities){
            DutyStudentJson dutyStudentJson = new DutyStudentJson();
            dutyStudentJson.setStudentId(dutyStudentEntity.getStudentId());
            dutyStudentJson.setOperDate(dutyStudentEntity.getOperDate());
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
    public Response deleteDutyStudent(@QueryParam("studentId")Long studentId){
        dutyStudentRepository.deleteByStudentId(studentId);
        return Response.ok().build();
    }

    @GET
    @Path("/statistics/range_query")
    public Response rangeQueryStatistics(@QueryParam("startDate")Date startDate,
                                         @QueryParam("endDate")Date endDate) {
        System.out.println("AA:"+startDate);
        List<Object[]> statisticRangeDataList = statisticsRepository.getRangeStatistics(
                startDate, endDate);
        return Response.ok().entity(statisticsService.object2Json(statisticRangeDataList)).build();
    }
}

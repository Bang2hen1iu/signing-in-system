package com.xuemiao.api;

import com.xuemiao.api.Json.*;
import com.xuemiao.exception.*;
import com.xuemiao.model.pdm.*;
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
    SignInInfoV2Repository signInInfoV2Repository;
    @Autowired
    SignInInfoRecordRepository signInInfoRecordRepository;

    private NewCookie getCookie() {
        return cookieValidationService.getTokenCookie(cookiePath, superAdminCookieAge);
    }

    private NewCookie refreshCookie(String tokenString) {
        return cookieValidationService.refreshCookie(tokenString, cookiePath, superAdminCookieAge);
    }

    private void saveCoursePerWeekJson(Long courseId, CoursePerWeekJson coursePerWeekJson) {
        CoursePerWeekEntity coursePerWeekEntity = new CoursePerWeekEntity();
        coursePerWeekEntity.setCourseId(courseId);
        coursePerWeekEntity.setStartSection(coursePerWeekJson.getStartSection());
        coursePerWeekEntity.setEndSection(coursePerWeekJson.getEndSection());
        coursePerWeekEntity.setWeekday(coursePerWeekJson.getWeekday());
        coursePerWeekRepository.save(coursePerWeekEntity);
    }

    /*---------------------------------------------------------------------------------------------------------------*/

    //admin validation by token
    @GET
    @Path("/admin/token_validation")
    public Response adminTokenValidation(@CookieParam("token") String tokenString)
            throws AdminTokenWrongException, TokenInvalidException {
        cookieValidationService.checkTokenCookie(tokenString);
        return Response.ok().cookie(refreshCookie(tokenString)).build();
    }

    //admin validation by password
    @POST
    @Path("/admin/validation")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adminValidation(IdPasswordJson idPasswordJson)
            throws IdNotExistException, PasswordErrorException {
        adminValidationService.testPassword(idPasswordJson.getId(), idPasswordJson.getPassword());
        return Response.ok().cookie(getCookie()).build();
    }

    //admin logout
    @DELETE
    @Path("/admin/logout")
    public Response adminLogout(@CookieParam("token") String tokenString) {
        cookieValidationService.deleteCookieByToken(tokenString);
        return Response.ok().build();
    }

    /*---------------------------------------------------------------------------------------------------------------*/

    //add student
    @POST
    @Path("/students/addition")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStudent(StudentEntity studentEntity,
                               @CookieParam("token") String tokenString)
            throws StudentAdditionException, TokenInvalidException{
        cookieValidationService.checkTokenCookie(tokenString);
        if (studentRepository.save(studentEntity) == null) {
            throw new StudentAdditionException();
        }
        SignInInfoV2Entity signInInfoV2Entity = new SignInInfoV2Entity();
        signInInfoV2Entity.setStudentId(studentEntity.getStudentId());
        signInInfoV2Entity.setOperDate(new Date(DateTime.now().getMillis()));
        signInInfoV2Repository.save(signInInfoV2Entity);
        return Response.ok().cookie(refreshCookie(tokenString)).build();
    }

    //register student
    @POST
    @Path("/students/registering")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerStudent(RegisterStudentJson registerStudentJson,
                                    @CookieParam("token") String tokenString)
            throws TokenInvalidException{
        cookieValidationService.checkTokenCookie(tokenString);
        StudentEntity studentEntity = studentRepository.findOne(registerStudentJson.getStudentId());
        //TODO save fingerprint
        studentRepository.save(studentEntity);
        return Response.ok().cookie(refreshCookie(tokenString)).build();
    }

    //delete student
    @DELETE
    @Path("/students/deletion/{id}")
    public Response deleteStudent(@PathParam("id") Long id,
                                  @CookieParam("token") String tokenString)
            throws TokenInvalidException{
        cookieValidationService.checkTokenCookie(tokenString);
        dutyStudentRepository.deleteByStudentId(id);
        List<CourseEntity> courseEntities = courseRepository.findByStudentId(id);
        for (CourseEntity courseEntity : courseEntities) {
            coursePerWeekRepository.deleteByCourseId(courseEntity.getId());
        }
        courseRepository.deleteByStudentId(id);
        statisticsRepository.deleteByStudentId(id);
        absenceRepository.deleteByStudentId(id);
        List<SignInInfoV2Entity> signInInfoV2Entities = signInInfoV2Repository.findByStudentId(id);
        for(SignInInfoV2Entity signInInfoV2Entity : signInInfoV2Entities){
            signInInfoRecordRepository.deleteBySignInInfoId(signInInfoV2Entity.getId());
        }
        signInInfoV2Repository.deleteByStudentId(id);

        studentRepository.delete(id);
        return Response.ok().cookie(refreshCookie(tokenString)).build();
    }

    /*---------------------------------------------------------------------------------------------------------------*/

    //add course
    @POST
    @Path("/courses/addition")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCourse(CoursesInfoJson coursesInfoJson,
                              @CookieParam("token") String tokenString)
            throws TokenInvalidException{
        cookieValidationService.checkTokenCookie(tokenString);
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
        return Response.ok().cookie(refreshCookie(tokenString)).build();
    }

    //modify course
    @PUT
    @Path("/courses/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyCourse(CoursesInfoJson coursesInfoJson,
                                 @CookieParam("token") String tokenString)
            throws TokenInvalidException{
        cookieValidationService.checkTokenCookie(tokenString);
        CourseEntity courseEntity = courseRepository.findOne(coursesInfoJson.getId());
        courseEntity.setStartWeek(coursesInfoJson.getStartWeek());
        courseEntity.setEndWeek(coursesInfoJson.getEndWeek());
        courseRepository.save(courseEntity);
        coursePerWeekRepository.deleteByCourseId(coursesInfoJson.getId());
        for (CoursePerWeekJson coursePerWeekJson : coursesInfoJson.getCoursePerWeekJsonList()) {
            saveCoursePerWeekJson(coursesInfoJson.getId(), coursePerWeekJson);
        }
        return Response.ok().cookie(refreshCookie(tokenString)).build();
    }

    //delete course
    @DELETE
    @Path("/courses/deletion/{id}")
    public Response deleteCourse(@PathParam("id") Long id,
                                 @CookieParam("token") String tokenString)
            throws TokenInvalidException{
        cookieValidationService.checkTokenCookie(tokenString);
        coursePerWeekRepository.deleteByCourseId(id);
        courseRepository.delete(id);
        return Response.ok().cookie(refreshCookie(tokenString)).build();
    }

    /*---------------------------------------------------------------------------------------------------------------*/

    //add duty student
    @POST
    @Path("/duty_students/addition")
    public Response addDutyStudent(DutyStudentEntity dutyStudentEntity,
                                   @CookieParam("token") String tokenString)
            throws TokenInvalidException{
        cookieValidationService.checkTokenCookie(tokenString);
        dutyStudentRepository.save(dutyStudentEntity);
        return Response.ok().cookie(refreshCookie(tokenString)).build();
    }

    //delete duty student
    @DELETE
    @Path("/duty_students/deletion/{studentId}")
    public Response deleteDutyStudent(@PathParam("studentId") Long studentId,
                                      @CookieParam("token") String tokenString)
            throws TokenInvalidException{
        cookieValidationService.checkTokenCookie(tokenString);
        dutyStudentRepository.deleteByStudentId(studentId);
        return Response.ok().cookie(refreshCookie(tokenString)).build();
    }

}

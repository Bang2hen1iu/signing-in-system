package com.xuemiao.api;

import com.xuemiao.api.Json.IdPasswordJson;
import com.xuemiao.exception.IdNotExistException;
import com.xuemiao.exception.PasswordErrorException;
import com.xuemiao.exception.StudentAdditionException;
import com.xuemiao.model.pdm.*;
import com.xuemiao.model.repository.*;
import com.xuemiao.service.AdminValidationService;
import com.xuemiao.service.CookieValidationService;
import com.xuemiao.utils.DateUtils;
import com.xuemiao.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

/**
 * Created by dzj on 9/30/2016.
 */
@Path("/admin_api")
public class AdminApi {
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
    private final String cookiePath = "/api/admin_api";
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    DutyStudentRepository dutyStudentRepository;
    @Autowired
    StatisticsRepository statisticsRepository;

    @GET
    @Path("/{psw}")
    public String getPswHash(@PathParam("psw")String psw){
        return PasswordUtils.createPasswordHash(psw);
    }

    @POST
    @Path("/admin/validation")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adminValidation(IdPasswordJson idPasswordJson)
            throws IdNotExistException, PasswordErrorException{
        adminValidationService.testPassword(idPasswordJson.getId(), idPasswordJson.getPassword1(), 2);
        return Response.ok().cookie(getCookie(idPasswordJson.getId())).build();
    }

    private NewCookie getCookie(Long id){
        return cookieValidationService.getTokenCookie(id, cookiePath, superAdminCookieAge);
    }

    private NewCookie refreshCookie(String tokenString){
        return cookieValidationService.refreshCookie(tokenString, cookiePath, superAdminCookieAge);
    }

    @PUT
    @Path("/admin/password_update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adminPasswordUpdate(IdPasswordJson idPasswordJson)
    throws IdNotExistException, PasswordErrorException{
        adminValidationService.testPassword(idPasswordJson.getId(), idPasswordJson.getPassword1(), 2);
        SysAdminEntity sysAdminEntity = sysAdminRepository.findOne(idPasswordJson.getId());
        sysAdminEntity.setPasswordSalted(PasswordUtils.createPasswordHash(idPasswordJson.getPassword2()));
        sysAdminRepository.save(sysAdminEntity);
        return Response.ok().build();
    }

    @POST
    @Path("/student/addition")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStudent(StudentEntity studentEntity)
    throws StudentAdditionException{
        if(studentRepository.save(studentEntity)==null){
            throw new StudentAdditionException();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/student/deletion/{id}")
    public Response deleteStudent(@PathParam("id")Long id){
        studentRepository.delete(id);
        return Response.ok().build();
    }

    @POST
    @Path("/courses/addition")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCourse(CourseEntity courseEntity){
        courseRepository.save(courseEntity);
        return Response.ok().build();
    }

    @POST
    @Path("/duty_student/addition")
    public Response addDutyStudent(DutyStudentEntity dutyStudentEntity){
        dutyStudentRepository.save(dutyStudentEntity);
        return Response.ok().build();
    }

    private class statisticRangeQuery{
        private String startDate;
        private String endDate;

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }
    @GET
    @Path("/statistics/range_query")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response rangeQueryStatistics(statisticRangeQuery statisticRangeQuery){
        List<StatisticRangeData> statisticRangeDataList = statisticsRepository.getRangeStatistics(
                DateUtils.parseDateString(statisticRangeQuery.getStartDate()),
                DateUtils.parseDateString(statisticRangeQuery.getEndDate()));
        return Response.ok().entity(statisticRangeDataList).build();
    }
}

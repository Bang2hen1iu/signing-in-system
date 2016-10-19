package com.xuemiao.api;

import com.xuemiao.api.Json.RegisterStudentJson;
import com.xuemiao.exception.TokenInvalidException;
import com.xuemiao.model.pdm.DutyStudentEntity;
import com.xuemiao.model.pdm.StudentEntity;
import com.xuemiao.service.CookieService;
import com.xuemiao.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Date;

/**
 * Created by root on 16-10-19.
 */
@Path("/students")
public class StudentsApi {
    @Autowired
    CookieService cookieService;
    @Autowired
    StudentsService studentsService;

    //get student
    @GET
    public Response getStudents() {
        return Response.ok().entity(studentsService.getAllStudent()).build();
    }

    //get duty student by date
    @GET
    @Path("/duty_students/{date}")
    public Response getDutyStudents(@PathParam("date") Date date) {
        return Response.ok().entity(studentsService.getDutyStudentByDate(date)).build();
    }

    //get all duty student
    @GET
    @Path("/duty_students")
    public Response getDutyStudents() {
        return Response.ok().entity(studentsService.getAllDutyStudent()).build();
    }

    //add student
    @POST
    @Path("/addition")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStudent(StudentEntity studentEntity,
                               @CookieParam("token") String tokenString) throws TokenInvalidException {
        cookieService.checkTokenCookie(tokenString);
        studentsService.addStudent(studentEntity);
        return Response.ok().cookie(cookieService.refreshCookie(tokenString)).build();
    }

    //register student
    @POST
    @Path("/registering")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerStudent(RegisterStudentJson registerStudentJson,
                                    @CookieParam("token") String tokenString) throws TokenInvalidException {
        cookieService.checkTokenCookie(tokenString);
        studentsService.registerStudent(registerStudentJson);
        return Response.ok().cookie(cookieService.refreshCookie(tokenString)).build();
    }

    //delete student
    @DELETE
    @Path("/deletion/{id}")
    public Response deleteStudent(@PathParam("id") Long id,
                                  @CookieParam("token") String tokenString) throws TokenInvalidException {
        cookieService.checkTokenCookie(tokenString);
        studentsService.deleteStudentById(id);
        return Response.ok().cookie(cookieService.refreshCookie(tokenString)).build();
    }

    //add duty student
    @POST
    @Path("/duty_students/addition")
    public Response addDutyStudent(DutyStudentEntity dutyStudentEntity,
                                   @CookieParam("token") String tokenString)
            throws TokenInvalidException {
        cookieService.checkTokenCookie(tokenString);
        studentsService.addDutyStudent(dutyStudentEntity);
        return Response.ok().cookie(cookieService.refreshCookie(tokenString)).build();
    }

    //delete duty student
    @DELETE
    @Path("/duty_students/deletion/{studentId}")
    public Response deleteDutyStudent(@PathParam("studentId") Long studentId,
                                      @CookieParam("token") String tokenString)
            throws TokenInvalidException {
        cookieService.checkTokenCookie(tokenString);
        studentsService.deleteStudentById(studentId);
        return Response.ok().cookie(cookieService.refreshCookie(tokenString)).build();
    }
}

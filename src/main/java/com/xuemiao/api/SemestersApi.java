package com.xuemiao.api;

import com.xuemiao.api.Json.CoursesInfoJson;
import com.xuemiao.exception.TokenInvalidException;
import com.xuemiao.model.pdm.SemesterEntity;
import com.xuemiao.model.repository.SemesterRepository;
import com.xuemiao.service.CookieService;
import com.xuemiao.service.CoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by zijun on 17-3-19.
 */
@Path("/semesters")
public class SemestersApi {
    @Autowired
    SemesterRepository semesterRepository;
    @Autowired
    CookieService cookieService;
    @Autowired
    CoursesService coursesService;

    @GET
    public Response getSemesters(){
        return Response.ok().entity(semesterRepository.findAll()).build();
    }

    @POST
    @Path("/addition")
    public Response addSemester(SemesterEntity semesterEntity, @CookieParam("token") String tokenString)
            throws TokenInvalidException {
        cookieService.checkTokenCookie(tokenString);
        semesterRepository.save(semesterEntity);
        return Response.ok().cookie(cookieService.refreshCookie(tokenString)).build();
    }

    @DELETE
    @Path("/deletion/{id}")
    public Response deleteSemester(@PathParam("id") Long id, @CookieParam("token") String tokenString)
            throws TokenInvalidException{
        cookieService.checkTokenCookie(tokenString);
        coursesService.deleteCourseBySemesterId(id);
        semesterRepository.delete(id);
        return Response.ok().cookie(cookieService.refreshCookie(tokenString)).build();
    }

}

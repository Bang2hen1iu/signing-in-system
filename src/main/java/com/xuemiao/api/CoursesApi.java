package com.xuemiao.api;

import com.xuemiao.api.Json.CoursesInfoJson;
import com.xuemiao.exception.TokenInvalidException;
import com.xuemiao.service.CookieService;
import com.xuemiao.service.CoursesService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by root on 16-10-19.
 */
@Path("/courses")
public class CoursesApi {
    @Autowired
    CookieService cookieService;
    @Autowired
    CoursesService coursesService;

    //get course by student id
    @GET
    @Path("/{studentId}")
    public Response getCourseByStudentId(@PathParam("studentId") Long studentId) {
        return Response.ok().entity(coursesService.getCoursesJsonsByStudentId(studentId)).build();
    }

    //add course
    @POST
    @Path("/addition")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCourse(CoursesInfoJson coursesInfoJson,
                              @CookieParam("token") String tokenString)
            throws TokenInvalidException {
        cookieService.checkTokenCookie(tokenString);
        coursesService.addCourse(coursesInfoJson);
        return Response.ok().cookie(cookieService.refreshCookie(tokenString)).build();
    }

    //modify course
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyCourse(CoursesInfoJson coursesInfoJson,
                                 @CookieParam("token") String tokenString)
            throws TokenInvalidException {
        cookieService.checkTokenCookie(tokenString);
        coursesService.modifyCourse(coursesInfoJson);
        return Response.ok().cookie(cookieService.refreshCookie(tokenString)).build();
    }

    //delete course
    @DELETE
    @Path("/deletion/{id}")
    public Response deleteCourse(@PathParam("id") Long id,
                                 @CookieParam("token") String tokenString)
            throws TokenInvalidException {
        cookieService.checkTokenCookie(tokenString);
        coursesService.deleteCourseByCourseId(id);
        return Response.ok().cookie(cookieService.refreshCookie(tokenString)).build();
    }
}

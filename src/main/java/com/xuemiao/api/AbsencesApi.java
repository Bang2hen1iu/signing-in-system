package com.xuemiao.api;

import com.xuemiao.api.Json.AbsenceReasonJson;
import com.xuemiao.service.AbsencesService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by root on 16-10-19.
 */
@Path("/absences")
public class AbsencesApi {
    @Autowired
    AbsencesService absencesService;

    //add absence
    @POST
    @Path("/addition")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStudentAbsence(AbsenceReasonJson absenceReasonJson) {
        absencesService.addAbsence(absenceReasonJson);
        return Response.ok().build();
    }

    //get absence of student by date
    @GET
    @Path("/{studentId}")
    public Response getStudentAbsence(@PathParam("studentId") Long studentId,
                                      @QueryParam("operDate") String operDate) {
        return Response.ok().entity(absencesService.getAbsenceByIdAndDate(studentId, operDate)).build();
    }
}

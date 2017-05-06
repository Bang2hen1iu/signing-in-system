package com.xuemiao.api;

import com.xuemiao.api.Json.AbsenceReasonJson;
import com.xuemiao.exception.AskForAbsenceException;
import com.xuemiao.service.AbsencesService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
    public Response addStudentAbsence(AbsenceReasonJson absenceReasonJson) throws AskForAbsenceException{
        if (absencesService.addAbsence(absenceReasonJson)){
            return Response.ok().build();
        }
        else{
            throw new AskForAbsenceException();
        }

    }

}

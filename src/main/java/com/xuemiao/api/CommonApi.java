package com.xuemiao.api;

import com.xuemiao.api.Json.FingerprintJson;
import com.xuemiao.utils.PasswordUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by dzj on 10/1/2016.
 */
@Component
@Path("/common")
public class CommonApi {

    @PUT
    @Path("transfering")
    public Response transferSignInInfoData() {
        return Response.ok().build();
    }

    @GET
    @Path("/{psw}")
    public String getPswHash(@PathParam("psw") String psw) {
        return PasswordUtils.createPasswordHash(psw);
    }

    @POST
    @Path("/test_sign_in")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response testSignIn(FingerprintJson fingerprintJson) {
        return null;
    }

    //get system time of server
    @GET
    @Path("/system_time")
    public Response getSystemTime() {
        DateTime now = DateTime.now();
        return Response.ok().entity(now.getYear() + "-" + String.format("%02d", now.getMonthOfYear()) + "-" + String.format("%02d", now.getDayOfMonth()) + " " + String.format("%02d", now.getHourOfDay()) + ":" + String.format("%02d", now.getMinuteOfHour()) + ":" + String.format("%02d", now.getSecondOfMinute())).build();
    }

}

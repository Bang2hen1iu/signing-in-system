package com.xuemiao.api;

import com.xuemiao.api.Json.SignInActionJson;
import com.xuemiao.exception.StudentNotExistException;
import com.xuemiao.exception.TokenInvalidException;
import com.xuemiao.service.SignInInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Date;

/**
 * Created by dzj on 9/30/2016.
 */
@Component
@Path("/sign_in_info")
public class SignInInfoApi {
    @Autowired
    SignInInfoService signInInfoService;

    //add sign in info
    @POST
    @Path("/addition")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSignIn(SignInActionJson signInActionJson) throws IOException, TokenInvalidException,StudentNotExistException {
        signInInfoService.signIn(signInActionJson);
        return Response.ok().build();
    }

    //get sign in info by date
    @GET
    @Path("/{date}")
    public Response getSignInInfoV2(@PathParam("date") Date date) {
        return Response.ok().entity(signInInfoService.getSignInInfoJsonsByDate(date)).build();
    }

    //get latest date of sign in info
    @GET
    @Path("/latest_date")
    public Response getSignInInfoLatestDate() {
        return Response.ok().entity(signInInfoService.getSignInInfoLatestDate()).build();
    }

}

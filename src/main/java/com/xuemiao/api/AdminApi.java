package com.xuemiao.api;

import com.xuemiao.api.Json.IdPasswordJson;
import com.xuemiao.exception.AdminTokenWrongException;
import com.xuemiao.exception.IdNotExistException;
import com.xuemiao.exception.PasswordErrorException;
import com.xuemiao.exception.TokenInvalidException;
import com.xuemiao.service.AdminService;
import com.xuemiao.service.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by dzj on 9/30/2016.
 */
@Component
@Path("/admin")
public class AdminApi {
    @Autowired
    CookieService cookieService;
    @Autowired
    AdminService adminService;

    //admin validation by token
    @GET
    @Path("/token_validation")
    public Response adminTokenValidation(@CookieParam("token") String tokenString)
            throws AdminTokenWrongException, TokenInvalidException {
        cookieService.checkTokenCookie(tokenString);
        return Response.ok().cookie(cookieService.refreshCookie(tokenString)).build();
    }

    //admin validation by password
    @POST
    @Path("/validation")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adminValidation(IdPasswordJson idPasswordJson)
            throws IdNotExistException, PasswordErrorException {
        adminService.testPassword(idPasswordJson.getId(), idPasswordJson.getPassword());
        return Response.ok().cookie(cookieService.getCookie()).build();
    }

    //admin logout
    @DELETE
    @Path("/logout")
    public Response adminLogout(@CookieParam("token") String tokenString) {
        cookieService.deleteCookieByToken(tokenString);
        return Response.ok().build();
    }

}

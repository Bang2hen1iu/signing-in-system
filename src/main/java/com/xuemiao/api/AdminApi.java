package com.xuemiao.api;

import com.xuemiao.exception.IdNotExistException;
import com.xuemiao.exception.PasswordErrorException;
import com.xuemiao.exception.StudentAdditionException;
import com.xuemiao.model.pdm.StudentEntity;
import com.xuemiao.model.pdm.SysAdminEntity;
import com.xuemiao.model.repository.StudentRepository;
import com.xuemiao.model.repository.SysAdminRepository;
import com.xuemiao.service.AdminValidationService;
import com.xuemiao.service.CookieValidationService;
import com.xuemiao.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
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

    @GET
    @Path("/{psw}")
    public String getPswHash(@PathParam("psw")String psw){
        return PasswordUtils.createPasswordHash(psw);
    }

    @POST
    @Path("/admin/validation/{id}/{password}")
    public Response adminValidation(@PathParam("id") Long id,
                                    @PathParam("password") String password)
            throws IdNotExistException, PasswordErrorException{
        adminValidationService.testPassword(id, password, 1);
        return Response.ok().cookie(cookieValidationService.getTokenCookie(id, "/api/admin_api", 1800)).build();
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
}

package com.xuemiao.api;

import com.xuemiao.exception.IdNotExistException;
import com.xuemiao.exception.PasswordErrorException;
import com.xuemiao.exception.StudentAdditionException;
import com.xuemiao.model.pdm.StudentEntity;
import com.xuemiao.model.pdm.SysAdminEntity;
import com.xuemiao.model.repository.StudentRepository;
import com.xuemiao.model.repository.SysAdminRepository;
import com.xuemiao.service.AdminValidationService;
import com.xuemiao.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;

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

    @GET
    @Path("/{psw}")
    public String getPswHash(@PathParam("psw")String psw){
        return PasswordUtils.createPasswordHash(psw);
    }

    @POST
    @Path("/admin/validation")
    public Response adminValidation(@FormParam("id")Long id,
                                    @FormParam("password")String password,
                                    @FormParam("type")int type)
    throws IdNotExistException, PasswordErrorException{
        adminValidationService.testPassword(id, password, type);
        UUID token = UUID.randomUUID();
        Cookie cookie = new Cookie("token", token.toString(), "/", null); // 把token的path设置成"/"
        return Response.ok().cookie(new NewCookie(cookie)).build();
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

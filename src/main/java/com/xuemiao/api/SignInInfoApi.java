package com.xuemiao.api;

import com.xuemiao.exception.DateFormatErrorException;
import com.xuemiao.exception.IdNotExistException;
import com.xuemiao.exception.PasswordErrorException;
import com.xuemiao.model.pdm.DutyStudentEntity;
import com.xuemiao.model.pdm.SignInInfoEntity;
import com.xuemiao.model.repository.DutyStudentRepository;
import com.xuemiao.model.repository.SignInInfoRepository;
import com.xuemiao.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by dzj on 9/30/2016.
 */
@Path("/sign_in_info_api")
public class SignInInfoApi {
    @Autowired
    DutyStudentRepository dutyStudentRepository;
    @Autowired
    SignInInfoRepository signInInfoRepository;

    @GET
    @Path("/duty_students")
    public Response getDutyStudents(@QueryParam("date")String dateString)
    throws DateFormatErrorException{
        Date date = DateUtils.parseDateString(dateString);
        if(date==null){
            throw new DateFormatErrorException();
        }
        List<DutyStudentEntity> dutyStudentEntities = dutyStudentRepository.findByOperDate(date);
        return Response.ok().entity(dutyStudentEntities).build();
    }

    @GET
    @Path("/sign_in_info")
    public Response getSignInInfo(@QueryParam("date")String dateString)
    throws DateFormatErrorException{
        Date date = DateUtils.parseDateString(dateString);
        if(date==null){
            throw new DateFormatErrorException();
        }
        List<SignInInfoEntity> signInInfoEntities = signInInfoRepository.findByOperDate(date);
        return Response.ok().entity(signInInfoEntities).build();
    }
}

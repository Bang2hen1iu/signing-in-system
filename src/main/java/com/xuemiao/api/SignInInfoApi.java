package com.xuemiao.api;

import com.xuemiao.exception.DateFormatErrorException;
import com.xuemiao.exception.IdNotExistException;
import com.xuemiao.exception.PasswordErrorException;
import com.xuemiao.model.pdm.AbsenceEntity;
import com.xuemiao.model.pdm.DutyStudentEntity;
import com.xuemiao.model.pdm.SignInInfoEntity;
import com.xuemiao.model.pdm.StudentIdAndOperDateKey;
import com.xuemiao.model.repository.AbsenceRepository;
import com.xuemiao.model.repository.DutyStudentRepository;
import com.xuemiao.model.repository.SignInInfoRepository;
import com.xuemiao.exception.SignInOrderException;
import com.xuemiao.service.AdminValidationService;
import com.xuemiao.service.CookieValidationService;
import com.xuemiao.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by dzj on 9/30/2016.
 */
@Path("/sign_in_info_api")
public class SignInInfoApi {
    @Autowired
    DutyStudentRepository dutyStudentRepository;
    @Autowired
    SignInInfoRepository signInInfoRepository;
    @Autowired
    AdminValidationService adminValidationService;
    @Autowired
    CookieValidationService cookieValidationService;
    @Autowired
    AbsenceRepository absenceRepository;

    @POST
    @Path("/test")
    public Response testCookie(@CookieParam("token") String tokenString){
        Response loginResponse = cookieValidationService.checkTokenCookie(tokenString, 1);
        if(loginResponse!=null){
            return loginResponse;
        }
        return Response.ok().entity(tokenString).build();
    }

    @POST
    @Path("/admin/validation/{id}/{password}")
    public Response adminValidation(@PathParam("id") Long id,
                                    @PathParam("password") String password)
            throws IdNotExistException, PasswordErrorException{
        adminValidationService.testPassword(id, password, 1);
        return Response.ok().cookie(cookieValidationService.getTokenCookie(id, "/api/sign_in_info_api", 604800)).build();
    }

    @GET
    @Path("/duty_students/{date}")
    public Response getDutyStudents(@PathParam("date")String dateString)
    throws DateFormatErrorException{
        Date date = DateUtils.parseDateString(dateString);
        if(date==null){
            throw new DateFormatErrorException();
        }
        List<DutyStudentEntity> dutyStudentEntities = dutyStudentRepository.findByOperDate(date);
        return Response.ok().entity(dutyStudentEntities).build();
    }

    @GET
    @Path("/sign_in_info/{date}")
    public Response getSignInInfo(@PathParam("date")String dateString)
    throws DateFormatErrorException{
        Date date = DateUtils.parseDateString(dateString);
        if(date==null){
            throw new DateFormatErrorException();
        }
        List<SignInInfoEntity> signInInfoEntities = signInInfoRepository.findByOperDate(date);
        return Response.ok().entity(signInInfoEntities).build();
    }

    @POST
    @Path("/sign_in_info/addition/{studentId}/{operDate}/{signInOrder}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSignIn(@PathParam("studentId")Long studentId,
                              @PathParam("operDate")String operDate,
                              @PathParam("signInOrder")int signInOrder)
            throws SignInOrderException{
        StudentIdAndOperDateKey studentIdAndOperDateKey = new StudentIdAndOperDateKey();
        studentIdAndOperDateKey.setStudentId(studentId);
        studentIdAndOperDateKey.setOperDate(DateUtils.parseDateString(operDate));
        SignInInfoEntity signInInfoEntity = signInInfoRepository.findOne(studentIdAndOperDateKey);
        Timestamp now = new Timestamp(new Date().getTime());
        switch (signInOrder){
            case 1:
                signInInfoEntity.setStartMorning(now);
                break;
            case 2:
                signInInfoEntity.setEndMorning(now);
                break;
            case 3:
                signInInfoEntity.setStartAfternoon(now);
                break;
            case 4:
                signInInfoEntity.setEndAfternoon(now);
                break;
            case 5:
                signInInfoEntity.setStartNight(now);
                break;
            case 6:
                signInInfoEntity.setEndNight(now);
                break;
            default:
                throw new SignInOrderException();
        }
        signInInfoRepository.save(signInInfoEntity);
        return Response.ok().build();
    }

    @POST
    @Path("/absences/{studentId}/{operDate}")
    public Response getStudentAbsence(@PathParam("studentId")Long studentId,
                                      @PathParam("operDate")String operDate){
        StudentIdAndOperDateKey studentIdAndOperDateKey = new StudentIdAndOperDateKey();
        studentIdAndOperDateKey.setStudentId(studentId);
        studentIdAndOperDateKey.setOperDate(DateUtils.parseDateString(operDate));
        AbsenceEntity absenceEntity = absenceRepository.findOne(studentIdAndOperDateKey);
        return Response.ok().entity(absenceEntity).build();
    }



}

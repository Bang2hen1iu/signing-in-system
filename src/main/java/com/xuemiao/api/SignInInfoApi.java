package com.xuemiao.api;

import com.xuemiao.api.Json.AbsenceReasonJson;
import com.xuemiao.api.Json.IdPasswordJson;
import com.xuemiao.api.Json.SignInActionJson;
import com.xuemiao.exception.IdNotExistException;
import com.xuemiao.exception.PasswordErrorException;
import com.xuemiao.exception.TokenInvalidException;
import com.xuemiao.model.pdm.*;
import com.xuemiao.model.pdm.primaryKey.FingerprintPK;
import com.xuemiao.model.pdm.primaryKey.StudentIdAndOperDateKey;
import com.xuemiao.model.repository.*;
import com.xuemiao.service.AdminValidationService;
import com.xuemiao.service.CookieValidationService;
import com.xuemiao.service.SignInService;
import com.xuemiao.utils.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by dzj on 9/30/2016.
 */
@Component
@Path("/sign_in_info_api")
public class SignInInfoApi {
    @Value("${admin.cookie.token.path}")
    String cookiePath;
    @Value("${admin.cookie.token.age}")
    int adminCookieAge;
    @Autowired
    SignInService signInService;
    @Autowired
    AdminValidationService adminValidationService;
    @Autowired
    CookieValidationService cookieValidationService;
    @Autowired
    AbsenceRepository absenceRepository;
    @Value("${signatureImgPath}")
    String signatureImgPath;
    @Autowired
    SignInInfoV2Repository signInInfoV2Repository;
    @Autowired
    SignInInfoRecordRepository signInInfoRecordRepository;
    @Autowired
    StudentRepository studentRepository;

    /*---------------------------------------------------------------------------------------------------------------*/

    @POST
    @Path("/test_sign_in")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response testSignIn(SignInActionJson signInActionJson){
        //StudentEntity studentEntity = studentRepository.findOneByFingerprint(signInActionJson.getFingerprint());

        //return Response.ok().entity(studentEntity).build();
        return null;
    }

    /*---------------------------------------------------------------------------------------------------------------*/

    //add sign in info
    @POST
    @Path("/sign_in_info/addition")
    public Response addSignIn(SignInActionJson signInActionJson)
            throws IOException, TokenInvalidException {
        DateTime dateTimeNow = DateTime.now();
        Long studentId = signInService.checkFingerPrint(signInActionJson.getFingerprint());
        SignInInfoV2Entity signInInfoV2Entity = signInInfoV2Repository.findOneByStudentIdAndDate(studentId,new Date(dateTimeNow.getMillis()));
        Timestamp now = new Timestamp(dateTimeNow.getMillis());
        SignInInfoRecordEntity signInInfoRecordEntity = signInInfoRecordRepository.findOneUnfinishedSignInRecord(signInInfoV2Entity.getId());
        if(signInInfoRecordEntity==null){
            signInInfoRecordEntity = new SignInInfoRecordEntity();
            signInInfoRecordEntity.setSignInInfoId(signInInfoV2Entity.getId());
            signInInfoRecordEntity.setStartTime(now);
        }
        else {
            signInInfoRecordEntity.setEndTime(now);
        }
        signInInfoRecordRepository.save(signInInfoRecordEntity);

        return Response.ok().build();
    }

    /*---------------------------------------------------------------------------------------------------------------*/

    //add absence
    @POST
    @Path("/absences/addition")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStudentAbsence(AbsenceReasonJson absenceReasonJson) {
        StudentIdAndOperDateKey studentIdAndOperDateKey = new StudentIdAndOperDateKey();
        studentIdAndOperDateKey.setStudentId(absenceReasonJson.getStudentId());
        studentIdAndOperDateKey.setOperDate(absenceReasonJson.getOperDate());
        AbsenceEntity originAbsenceEntity = absenceRepository.findOne(studentIdAndOperDateKey);
        if (originAbsenceEntity == null) {
            AbsenceEntity absenceEntity = new AbsenceEntity();
            absenceEntity.setStudentId(absenceReasonJson.getStudentId());
            absenceEntity.setOperDate(absenceReasonJson.getOperDate());
            absenceEntity.setAbsenceReason(absenceReasonJson.getAbsenceReason());
            absenceEntity.setStartAbsence(DateUtils.adjustYearMonthDay(absenceReasonJson.getStartAbsence()));
            absenceEntity.setEndAbsence(DateUtils.adjustYearMonthDay(absenceReasonJson.getEndAbsence()));
            absenceRepository.save(absenceEntity);
        } else {
            originAbsenceEntity.setAbsenceReason(absenceReasonJson.getAbsenceReason());
            originAbsenceEntity.setStartAbsence(DateUtils.adjustYearMonthDay(absenceReasonJson.getStartAbsence()));
            originAbsenceEntity.setEndAbsence(DateUtils.adjustYearMonthDay(absenceReasonJson.getEndAbsence()));
            absenceRepository.save(originAbsenceEntity);
        }
        return Response.ok().build();
    }
}

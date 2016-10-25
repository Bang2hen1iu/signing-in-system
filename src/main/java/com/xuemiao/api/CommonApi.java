package com.xuemiao.api;

import com.xuemiao.api.Json.FingerprintJson;
import com.xuemiao.model.pdm.SignInInfoEntity;
import com.xuemiao.model.pdm.SignInInfoRecordEntity;
import com.xuemiao.model.pdm.SignInInfoV2Entity;
import com.xuemiao.model.repository.SignInInfoRecordRepository;
import com.xuemiao.model.repository.SignInInfoRepository;
import com.xuemiao.model.repository.SignInInfoV2Repository;
import com.xuemiao.utils.PasswordUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by dzj on 10/1/2016.
 */
@Component
@Path("/common")
public class CommonApi {
    @Autowired
    SignInInfoRepository signInInfoRepository;
    @Autowired
    SignInInfoV2Repository signInInfoV2Repository;
    @Autowired
    SignInInfoRecordRepository signInInfoRecordRepository;

    @PUT
    @Path("/transfering")
    public Response transferSignInInfoData() {
        List<SignInInfoEntity> signInInfoEntities = signInInfoRepository.findAll();
        for(SignInInfoEntity signInInfoEntity : signInInfoEntities){
            SignInInfoV2Entity signInInfoV2Entity = new SignInInfoV2Entity();
            signInInfoV2Entity.setStudentId(signInInfoEntity.getStudentId());
            signInInfoV2Entity.setOperDate(signInInfoEntity.getOperDate());
            signInInfoV2Repository.save(signInInfoV2Entity);
            if(signInInfoEntity.getStartMorning()!=null&&signInInfoEntity.getEndMorning()!=null){
                SignInInfoRecordEntity signInInfoRecordEntity = new SignInInfoRecordEntity();
                signInInfoRecordEntity.setSignInInfoId(signInInfoV2Entity.getId());
                signInInfoRecordEntity.setStartTime(signInInfoEntity.getStartMorning());
                signInInfoRecordEntity.setEndTime(signInInfoEntity.getEndMorning());
                signInInfoRecordRepository.save(signInInfoRecordEntity);
            }
            if(signInInfoEntity.getStartAfternoon()!=null&&signInInfoEntity.getEndAfternoon()!=null){
                SignInInfoRecordEntity signInInfoRecordEntity = new SignInInfoRecordEntity();
                signInInfoRecordEntity.setSignInInfoId(signInInfoV2Entity.getId());
                signInInfoRecordEntity.setStartTime(signInInfoEntity.getStartAfternoon());
                signInInfoRecordEntity.setEndTime(signInInfoEntity.getEndAfternoon());
                signInInfoRecordRepository.save(signInInfoRecordEntity);
            }
            if(signInInfoEntity.getStartNight()!=null&&signInInfoEntity.getEndNight()!=null){
                SignInInfoRecordEntity signInInfoRecordEntity = new SignInInfoRecordEntity();
                signInInfoRecordEntity.setSignInInfoId(signInInfoV2Entity.getId());
                signInInfoRecordEntity.setStartTime(signInInfoEntity.getStartNight());
                signInInfoRecordEntity.setEndTime(signInInfoEntity.getEndNight());
                signInInfoRecordRepository.save(signInInfoRecordEntity);
            }
        }
        return Response.ok().entity("good").build();
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

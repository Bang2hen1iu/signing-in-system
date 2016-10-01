package com.xuemiao.api;

import com.xuemiao.exception.DateFormatErrorException;
import com.xuemiao.model.pdm.*;
import com.xuemiao.model.repository.*;
import com.xuemiao.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dzj on 10/1/2016.
 */
@Path("/common_api")
public class CommonApi {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    StatisticsRepository statisticsRepository;
    @Autowired
    DutyStudentRepository dutyStudentRepository;
    @Autowired
    SignInInfoRepository signInInfoRepository;
    @Autowired
    AbsenceRepository absenceRepository;

    @GET
    @Path("/students")
    public Response getStudents(){
        List<StudentEntity> studentEntities = studentRepository.findAll();
        return Response.ok().entity(studentEntities).build();
    }

    @GET
    @Path("/statistics/{date}")
    public Response getStatisticData(@PathParam("date")String dateString){
        Date date = DateUtils.parseDateString(dateString);
        List<StatisticsEntity> statisticsEntities = statisticsRepository.findByOperDate(date);
        return Response.ok().entity(statisticsEntities).build();
    }

    @GET
    @Path("/duty_students/{date}")
    public Response getDutyStudents(@PathParam("date")String dateString)
            throws DateFormatErrorException {
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

    @GET
    @Path("/absences/{studentId}/{operDate}")
    public Response getStudentAbsence(@PathParam("studentId")Long studentId,
                                      @PathParam("operDate")String operDate){
        StudentIdAndOperDateKey studentIdAndOperDateKey = new StudentIdAndOperDateKey();
        studentIdAndOperDateKey.setStudentId(studentId);
        studentIdAndOperDateKey.setOperDate(DateUtils.parseDateString(operDate));
        AbsenceEntity absenceEntity = absenceRepository.findOne(studentIdAndOperDateKey);
        return Response.ok().entity(absenceEntity).build();
    }

    @GET
    @Path("/sign_in_info/date")
    public Response getSignInInfoDate(){
        List<Date> dateList = signInInfoRepository.getAllSignInInfoDate();
        return Response.ok().entity(DateUtils.DateList2DateStringList(dateList)).build();
    }

    @GET
    @Path("/statistics/date")
    public Response getStatisticsDate(){
        List<Date> dateList = statisticsRepository.getAllStatisticsDate();
        return Response.ok().entity(DateUtils.DateList2DateStringList(dateList)).build();
    }
}

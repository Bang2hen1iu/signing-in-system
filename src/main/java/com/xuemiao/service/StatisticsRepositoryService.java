package com.xuemiao.service;

import com.xuemiao.api.Json.StatisticJson;
import com.xuemiao.model.pdm.AbsenceEntity;
import com.xuemiao.model.pdm.SignInInfoRecordEntity;
import com.xuemiao.model.pdm.SignInInfoV2Entity;
import com.xuemiao.model.pdm.StudentEntity;
import com.xuemiao.model.repository.AbsenceRepository;
import com.xuemiao.model.repository.SignInInfoRecordRepository;
import com.xuemiao.model.repository.SignInInfoV2Repository;
import com.xuemiao.model.repository.StudentRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16-10-25.
 */
@Service
public class StatisticsRepositoryService {
    @Autowired
    SignInInfoV2Repository signInInfoV2Repository;
    @Autowired
    AbsenceRepository absenceRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    SignInInfoRecordRepository signInInfoRecordRepository;

    public List<StatisticJson> getRangeStatistics(Date startDate, Date endDate){
        List<StatisticJson> statisticJsons = new ArrayList<>();
        List<StudentEntity> studentEntities = studentRepository.findAll();
        for(StudentEntity studentEntity : studentEntities){

            List<SignInInfoV2Entity> signInInfoV2Entities;
            if(endDate==null){
                signInInfoV2Entities = signInInfoV2Repository.findByStudentIdAndStartDate(studentEntity.getStudentId(), startDate);
            }
            else {
                signInInfoV2Entities = signInInfoV2Repository.findByStudentIdAndStartDateAndEndDate(
                        studentEntity.getStudentId(),startDate,endDate);
            }
            if(signInInfoV2Entities!=null){
                StatisticJson statisticJson = new StatisticJson();
                statisticJson.setName(studentEntity.getName());
                int absenceTimes = 0;
                double stayLabTime = 0.0;
                for(SignInInfoV2Entity signInInfoV2Entity : signInInfoV2Entities){
                    absenceTimes += absenceRepository.countBySignInInfoId(signInInfoV2Entity.getId());
                    List<SignInInfoRecordEntity> signInInfoRecordEntities = signInInfoRecordRepository.findBySignInInfoId(signInInfoV2Entity.getId());
                    for(SignInInfoRecordEntity signInInfoRecordEntity : signInInfoRecordEntities){
                        if(signInInfoRecordEntity.getEndTime()==null){
                            stayLabTime += (DateTime.now().getMillis() - signInInfoRecordEntity.getStartTime().getTime());
                        }
                        else {
                            stayLabTime += (signInInfoRecordEntity.getEndTime().getTime() - signInInfoRecordEntity.getStartTime().getTime());
                        }
                    }
                }
                stayLabTime /= 1000*3600;
                statisticJson.setAbsenceTimes(absenceTimes);
                statisticJson.setStayLabTime(String.format("%.2f",stayLabTime));
                statisticJsons.add(statisticJson);
            }
        }
        return statisticJsons;
    }

    public List<StatisticJson> getStatisticsByStartDateUpToNow(Date startDate){
        return getRangeStatistics(startDate,null);
    }
}

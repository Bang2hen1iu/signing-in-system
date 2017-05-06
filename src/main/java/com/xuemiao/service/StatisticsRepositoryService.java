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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<StatisticJson> getRangeStatistics(Date startDate, Date endDate) {
        List<StatisticJson> statisticJsons = new ArrayList<>();
        List<StudentEntity> studentEntities = studentRepository.findAll();
        for (StudentEntity studentEntity : studentEntities) {

            List<SignInInfoV2Entity> signInInfoV2Entities;
            if (endDate == null) {
                signInInfoV2Entities = signInInfoV2Repository.findByStudentIdAndStartDate(studentEntity.getStudentId(), startDate);
            } else {
                signInInfoV2Entities = signInInfoV2Repository.findByStudentIdAndStartDateAndEndDate(
                        studentEntity.getStudentId(), startDate, endDate);
            }
            if (signInInfoV2Entities != null) {
                StatisticJson statisticJson = new StatisticJson();
                statisticJson.setName(studentEntity.getName());
                double absenceTime = 0.0;
                double stayLabTime = 0.0;
                for (SignInInfoV2Entity signInInfoV2Entity : signInInfoV2Entities) {
                    List<AbsenceEntity> absenceEntities = absenceRepository.findBySignInInfoId(signInInfoV2Entity.getId());
                    for (AbsenceEntity absenceEntity : absenceEntities){
                        if (absenceEntity.isMakeUp()){
                            stayLabTime += (absenceEntity.getEndAbsence().getTime() - absenceEntity.getStartAbsence().getTime());
                        }
                        else{
                            absenceTime += (absenceEntity.getEndAbsence().getTime() - absenceEntity.getStartAbsence().getTime());
                        }
                    }
                    List<SignInInfoRecordEntity> signInInfoRecordEntities = signInInfoRecordRepository.findBySignInInfoId(signInInfoV2Entity.getId());
                    for (SignInInfoRecordEntity signInInfoRecordEntity : signInInfoRecordEntities) {
                        if (signInInfoRecordEntity.getEndTime() != null) {
                            stayLabTime += (signInInfoRecordEntity.getEndTime().getTime() - signInInfoRecordEntity.getStartTime().getTime());
                        }
                    }
                }
                absenceTime /= 1000 * 3600;
                stayLabTime /= 1000 * 3600;
                statisticJson.setAbsenceTime(absenceTime);
                statisticJson.setStayLabTime(stayLabTime);
                statisticJsons.add(statisticJson);
            }
        }
        return statisticJsons;
    }

    public List<StatisticJson> getStatisticsByStartDateUpToNow(Date startDate) {
        return getRangeStatistics(startDate, null);
    }
}

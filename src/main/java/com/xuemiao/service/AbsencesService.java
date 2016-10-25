package com.xuemiao.service;

import com.xuemiao.api.Json.AbsenceReasonJson;
import com.xuemiao.model.pdm.AbsenceEntity;
import com.xuemiao.model.pdm.SignInInfoV2Entity;
import com.xuemiao.model.pdm.primaryKey.StudentIdAndOperDateKey;
import com.xuemiao.model.repository.AbsenceRepository;
import com.xuemiao.model.repository.SignInInfoV2Repository;
import com.xuemiao.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

/**
 * Created by root on 16-10-19.
 */
@Service
public class AbsencesService {
    @Autowired
    AbsenceRepository absenceRepository;
    @Autowired
    SignInInfoV2Repository signInInfoV2Repository;
    @Autowired
    SignInInfoService signInInfoService;

    public void addAbsence(AbsenceReasonJson absenceReasonJson) {
        AbsenceEntity absenceEntity = new AbsenceEntity();
        SignInInfoV2Entity signInInfoV2Entity = signInInfoV2Repository.findOneByStudentIdAndDate(
                absenceReasonJson.getStudentId(),absenceReasonJson.getOperDate());
        if(signInInfoV2Entity==null){
            signInInfoV2Entity = signInInfoService.addSignInInfo(absenceReasonJson.getStudentId());
        }
        absenceEntity.setSignInInfoId(signInInfoV2Entity.getId());
        absenceEntity.setAbsenceReason(absenceReasonJson.getAbsenceReason());
        absenceEntity.setStartAbsence(DateUtils.adjustYearMonthDay(absenceReasonJson.getStartAbsence()));
        absenceEntity.setEndAbsence(DateUtils.adjustYearMonthDay(absenceReasonJson.getEndAbsence()));
        absenceRepository.save(absenceEntity);
    }

    public void deleteByStudentId(Long studentId){
        List<SignInInfoV2Entity> signInInfoV2Entities = signInInfoV2Repository.findByStudentId(studentId);
        for(SignInInfoV2Entity signInInfoV2Entity : signInInfoV2Entities){
            absenceRepository.deleteBySignInInfoId(signInInfoV2Entity.getId());
        }
    }

    public void deleteBySignInInfoId(Long signInInfoId){
        absenceRepository.deleteBySignInInfoId(signInInfoId);
    }

    public List<AbsenceEntity> getAbsenceBySignInInfoId(Long signInInfoId){
        return absenceRepository.findBySignInInfoId(signInInfoId);
    }
}

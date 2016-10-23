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

import java.sql.Date;
import java.util.List;

/**
 * Created by root on 16-10-19.
 */
@Component
public class AbsencesService {
    @Autowired
    AbsenceRepository absenceRepository;
    @Autowired
    SignInInfoV2Repository signInInfoV2Repository;

    public void addAbsence(AbsenceReasonJson absenceReasonJson) {
        SignInInfoV2Entity signInInfoV2Entity = signInInfoV2Repository.findOneByStudentIdAndDate(
                absenceReasonJson.getStudentId(),absenceReasonJson.getOperDate());
        AbsenceEntity originAbsenceEntity = absenceRepository.findOne(signInInfoV2Entity.getId());
        if (originAbsenceEntity == null) {
            AbsenceEntity absenceEntity = new AbsenceEntity();
            signInInfoV2Entity = signInInfoV2Repository.findOneByStudentIdAndDate(
                    absenceReasonJson.getStudentId(),absenceReasonJson.getOperDate());
            absenceEntity.setSignInInfoId(signInInfoV2Entity.getId());
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
    }

    public AbsenceEntity getAbsenceByIdAndDate(Long studentId, String operDate) {
        return getAbsenceByIdAndDate(studentId, new Date(DateUtils.parseDateString(operDate).getMillis()));
    }

    public AbsenceEntity getAbsenceByIdAndDate(Long studentId, Date operDate) {
        SignInInfoV2Entity signInInfoV2Entity = signInInfoV2Repository.findOneByStudentIdAndDate(studentId,operDate);
        return absenceRepository.findOne(signInInfoV2Entity.getId());
    }

    public void deleteByStudentId(Long studentId){
        List<SignInInfoV2Entity> signInInfoV2Entities = signInInfoV2Repository.findByStudentId(studentId);
        for(SignInInfoV2Entity signInInfoV2Entity : signInInfoV2Entities){
            absenceRepository.delete(signInInfoV2Entity.getId());
        }
    }
}

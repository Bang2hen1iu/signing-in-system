package com.xuemiao.service;

import com.xuemiao.api.Json.AbsenceReasonJson;
import com.xuemiao.model.pdm.AbsenceEntity;
import com.xuemiao.model.pdm.primaryKey.StudentIdAndOperDateKey;
import com.xuemiao.model.repository.AbsenceRepository;
import com.xuemiao.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;

/**
 * Created by root on 16-10-19.
 */
@Component
public class AbsencesService {
    @Autowired
    AbsenceRepository absenceRepository;

    public void addAbsence(AbsenceReasonJson absenceReasonJson) {
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
    }

    public AbsenceEntity getAbsenceByIdAndDate(Long studentId, String operDate) {
        return getAbsenceByIdAndDate(studentId, new Date(DateUtils.parseDateString(operDate).getMillis()));
    }

    public AbsenceEntity getAbsenceByIdAndDate(Long studentId, Date operDate) {
        StudentIdAndOperDateKey studentIdAndOperDateKey = new StudentIdAndOperDateKey();
        studentIdAndOperDateKey.setStudentId(studentId);
        studentIdAndOperDateKey.setOperDate(operDate);
        return absenceRepository.findOne(studentIdAndOperDateKey);
    }
}

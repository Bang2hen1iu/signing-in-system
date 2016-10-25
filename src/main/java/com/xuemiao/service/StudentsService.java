package com.xuemiao.service;

import com.xuemiao.api.Json.DutyStudentJson;
import com.xuemiao.api.Json.FingerprintJson;
import com.xuemiao.api.Json.RegisterStudentJson;
import com.xuemiao.exception.FingerprintInvalidException;
import com.xuemiao.model.pdm.DutyStudentEntity;
import com.xuemiao.model.pdm.FingerprintEntity;
import com.xuemiao.model.pdm.StudentEntity;
import com.xuemiao.model.repository.AbsenceRepository;
import com.xuemiao.model.repository.DutyStudentRepository;
import com.xuemiao.model.repository.FingerprintRepository;
import com.xuemiao.model.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16-10-19.
 */
@Service
public class StudentsService {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    DutyStudentRepository dutyStudentRepository;
    @Autowired
    SignInInfoService signInInfoService;
    @Autowired
    CoursesService coursesService;
    @Autowired
    AbsencesService absencesService;
    @Autowired
    AbsenceRepository absenceRepository;
    @Autowired
    FingerprintRepository fingerprintRepository;

    public List<StudentEntity> getAllStudent() {
        return studentRepository.findAll();
    }

    public List<FingerprintJson> getAllFingerPrint() {
        List<FingerprintEntity> fingerprintEntities = fingerprintRepository.findAll();
        List<FingerprintJson> fingerprintJsons = new ArrayList<>();
        for (FingerprintEntity fingerprintEntity : fingerprintEntities) {
            FingerprintJson fingerprintJson = new FingerprintJson();
            fingerprintJson.setFingerprint(fingerprintEntity.getToken());
            fingerprintJsons.add(fingerprintJson);
        }
        return fingerprintJsons;
    }

    public List<DutyStudentJson> getDutyStudentByDate(Date date) {
        List<DutyStudentEntity> dutyStudentEntities = dutyStudentRepository.findByOperDate(date);
        List<DutyStudentJson> dutyStudentJsonList = new ArrayList<>();
        for (DutyStudentEntity dutyStudentEntity : dutyStudentEntities) {
            dutyStudentJsonList.add(wrapDutyStudentIntoJson(dutyStudentEntity));
        }
        return dutyStudentJsonList;
    }

    public List<DutyStudentJson> getAllDutyStudent() {
        List<DutyStudentEntity> dutyStudentEntities = dutyStudentRepository.findAll();
        List<DutyStudentJson> dutyStudentJsonList = new ArrayList<>();
        for (DutyStudentEntity dutyStudentEntity : dutyStudentEntities) {
            dutyStudentJsonList.add(wrapDutyStudentIntoJson(dutyStudentEntity));
        }
        return dutyStudentJsonList;
    }

    public void addStudent(StudentEntity studentEntity) {
        studentRepository.save(studentEntity);
        signInInfoService.addSignInInfo(studentEntity.getStudentId());
    }

    public void registerStudent(RegisterStudentJson registerStudentJson) throws FingerprintInvalidException {
        if (registerStudentJson.getFingerprint() == null || registerStudentJson.getFingerprint().equals("")) {
            throw new FingerprintInvalidException();
        }
        FingerprintEntity fingerprintEntity = new FingerprintEntity();
        fingerprintEntity.setStudentId(registerStudentJson.getStudentId());
        fingerprintEntity.setToken(registerStudentJson.getFingerprint());
        fingerprintRepository.save(fingerprintEntity);
    }

    public void deleteStudentById(Long id) {
        dutyStudentRepository.deleteByStudentId(id);
        coursesService.deleteCourseByStudentId(id);
        absencesService.deleteByStudentId(id);
        signInInfoService.deleteSignInInfoByStudentId(id);
        studentRepository.delete(id);
    }

    public void addDutyStudent(DutyStudentEntity dutyStudentEntity) {
        dutyStudentRepository.save(dutyStudentEntity);
    }

    public void deleteDutyStudentById(Long studentId) {
        dutyStudentRepository.deleteByStudentId(studentId);
    }

    private DutyStudentJson wrapDutyStudentIntoJson(DutyStudentEntity dutyStudentEntity) {
        DutyStudentJson dutyStudentJson = new DutyStudentJson();
        dutyStudentJson.setStudentId(dutyStudentEntity.getStudentId());
        dutyStudentJson.setName(studentRepository.findOne(dutyStudentEntity.getStudentId()).getName());
        dutyStudentJson.setStartDate(dutyStudentEntity.getStartDate());
        dutyStudentJson.setEndDate(dutyStudentEntity.getEndDate());
        return dutyStudentJson;
    }

}

package com.xuemiao.service;

import com.xuemiao.api.Json.DutyStudentJson;
import com.xuemiao.api.Json.RegisterStudentJson;
import com.xuemiao.model.pdm.DutyStudentEntity;
import com.xuemiao.model.pdm.FingerprintEntity;
import com.xuemiao.model.pdm.StudentEntity;
import com.xuemiao.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16-10-19.
 */
@Component
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
    StatisticsRepository statisticsRepository;
    @Autowired
    AbsenceRepository absenceRepository;
    @Autowired
    FingerprintRepository fingerprintRepository;


    public List<StudentEntity> getAllStudent() {
        return studentRepository.findAll();
    }

    public List<String> getAllFingerPrint(){
        List<FingerprintEntity> fingerprintEntitiesTemp = fingerprintRepository.findAll();
        List<String> fingerprints = new ArrayList<>();
        for(FingerprintEntity fingerprintEntity : fingerprintEntitiesTemp){
            fingerprints.add(fingerprintEntity.getToken());
        }
        return fingerprints;
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
        signInInfoService.addStudentIntoSignInInfo(studentEntity);
    }

    public void registerStudent(RegisterStudentJson registerStudentJson) {
        StudentEntity studentEntity = studentRepository.findOne(registerStudentJson.getStudentId());
        //TODO save fingerprint
        studentRepository.save(studentEntity);
    }

    public void deleteStudentById(Long id) {
        dutyStudentRepository.deleteByStudentId(id);
        coursesService.deleteCourseByStudentId(id);
        statisticsRepository.deleteByStudentId(id);
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

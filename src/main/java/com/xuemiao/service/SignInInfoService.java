package com.xuemiao.service;

import com.xuemiao.api.Json.SignInActionJson;
import com.xuemiao.api.Json.SignInInfoCoursesInfo;
import com.xuemiao.api.Json.SignInInfoJson;
import com.xuemiao.model.pdm.*;
import com.xuemiao.model.pdm.primaryKey.CoursePerWeekPKey;
import com.xuemiao.model.repository.*;
import com.xuemiao.utils.DateUtils;
import com.xuemiao.utils.FingerprintUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16-10-19.
 */
@Component
public class SignInInfoService {
    @Autowired
    FingerprintRepository fingerprintRepository;
    @Autowired
    SignInInfoV2Repository signInInfoV2Repository;
    @Autowired
    SignInInfoRecordRepository signInInfoRecordRepository;
    @Autowired
    AbsencesService absencesService;
    @Value("${course.start_date}")
    String courseStartDateString;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    CoursePerWeekRepository coursePerWeekRepository;

    private Long checkFingerPrint(String fingerprint) {
        List<FingerprintEntity> fingerprintEntities = fingerprintRepository.findAll();
        for (FingerprintEntity fingerprintEntity : fingerprintEntities) {
            if (FingerprintUtils.process(fingerprintEntity.getToken(), fingerprint)) {
                return fingerprintEntity.getStudentId();
            }
        }
        return null;
    }

    public void signIn(SignInActionJson signInActionJson) {
        DateTime dateTimeNow = DateTime.now();
        Long studentId = this.checkFingerPrint(signInActionJson.getFingerprint());
        SignInInfoV2Entity signInInfoV2Entity = signInInfoV2Repository.findOneByStudentIdAndDate(studentId, new Date(dateTimeNow.getMillis()));
        Timestamp now = new Timestamp(dateTimeNow.getMillis());
        SignInInfoRecordEntity signInInfoRecordEntity = signInInfoRecordRepository.findOneUnfinishedSignInRecord(signInInfoV2Entity.getId());
        if (signInInfoRecordEntity == null) {
            signInInfoRecordEntity = new SignInInfoRecordEntity();
            signInInfoRecordEntity.setSignInInfoId(signInInfoV2Entity.getId());
            signInInfoRecordEntity.setStartTime(now);
        } else {
            signInInfoRecordEntity.setEndTime(now);
        }
        signInInfoRecordRepository.save(signInInfoRecordEntity);
    }

    public Date getSignInInfoLatestDate() {
        return signInInfoV2Repository.getLatestSignInInfoDate();
    }

    public List<SignInInfoJson> getSignInInfoJsonsByDate(Date date) {
        List<SignInInfoV2Entity> signInInfoV2Entities = signInInfoV2Repository.findByOperDate(new Date(DateTime.now().getMillis()));
        List<SignInInfoJson> signInInfoJsonList = new ArrayList<>();
        for (SignInInfoV2Entity signInInfoV2Entity : signInInfoV2Entities) {
            signInInfoJsonList.add(wrapSignInInfoIntoJson(signInInfoV2Entity, date));
        }
        return signInInfoJsonList;
    }

    public void addStudentIntoSignInInfo(StudentEntity studentEntity) {
        SignInInfoV2Entity signInInfoV2Entity = new SignInInfoV2Entity();
        signInInfoV2Entity.setStudentId(studentEntity.getStudentId());
        signInInfoV2Entity.setOperDate(new Date(DateTime.now().getMillis()));
        signInInfoV2Repository.save(signInInfoV2Entity);
    }

    public void deleteSignInInfoByStudentId(Long id) {
        List<SignInInfoV2Entity> signInInfoV2Entities = signInInfoV2Repository.findByStudentId(id);
        for (SignInInfoV2Entity signInInfoV2Entity : signInInfoV2Entities) {
            signInInfoRecordRepository.deleteBySignInInfoId(signInInfoV2Entity.getId());
        }
        signInInfoV2Repository.deleteByStudentId(id);
    }

    private SignInInfoJson wrapSignInInfoIntoJson(SignInInfoV2Entity signInInfoV2Entity, Date date) {
        DateTime dateTime = new DateTime(date);
        DateTime startDate = DateTime.parse(courseStartDateString);
        int currentWeek = DateUtils.getCurrentWeek(startDate, dateTime);
        int currentWeekday = DateUtils.getCurrentWeekDay(startDate, dateTime);

        SignInInfoJson signInInfoJson = new SignInInfoJson();
        signInInfoJson.setStudentId(signInInfoV2Entity.getStudentId().toString());
        signInInfoJson.setName(studentRepository.findOne(signInInfoV2Entity.getStudentId()).getName());
        signInInfoJson.setSignInInfoRecordEntities(signInInfoRecordRepository.findBySignInInfoId(signInInfoV2Entity.getId()));

        List<CourseEntity> courseEntities = courseRepository.getCoursesByStudentAndWeek(signInInfoV2Entity.getStudentId(), currentWeek);
        List<SignInInfoCoursesInfo> signInInfoCoursesInfoList = new ArrayList<>();
        for (CourseEntity courseEntity : courseEntities) {
            signInInfoCoursesInfoList.add(wrapCourseIntoSignInCourseInfoJson(courseEntity, currentWeekday));
        }
        signInInfoJson.setSignInInfoCoursesInfoList(signInInfoCoursesInfoList);

        AbsenceEntity absenceEntity = absencesService.getAbsenceByIdAndDate(signInInfoV2Entity.getStudentId(), signInInfoV2Entity.getOperDate());
        if (absenceEntity != null) {
            signInInfoJson.setStartAbsence(absenceEntity.getStartAbsence());
            signInInfoJson.setEndAbsence(absenceEntity.getEndAbsence());
            signInInfoJson.setAbsenceReason(absenceEntity.getAbsenceReason());
        }
        return signInInfoJson;
    }

    private SignInInfoCoursesInfo wrapCourseIntoSignInCourseInfoJson(CourseEntity courseEntity, int currentWeekday) {
        CoursePerWeekPKey coursePerWeekPKey = new CoursePerWeekPKey();
        coursePerWeekPKey.setCourseId(courseEntity.getId());
        coursePerWeekPKey.setWeekday(currentWeekday);
        CoursePerWeekEntity coursePerWeekEntity = coursePerWeekRepository.findOne(coursePerWeekPKey);
        SignInInfoCoursesInfo signInInfoCoursesInfo = new SignInInfoCoursesInfo();
        if (coursePerWeekEntity != null) {
            signInInfoCoursesInfo.setCourseName(courseEntity.getCourseName());
            signInInfoCoursesInfo.setStartSection(coursePerWeekEntity.getStartSection());
            signInInfoCoursesInfo.setEndSection(coursePerWeekEntity.getEndSection());
        }
        return signInInfoCoursesInfo;
    }

}

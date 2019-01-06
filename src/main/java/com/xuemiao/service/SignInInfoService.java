package com.xuemiao.service;

import com.xuemiao.api.Json.FingerprintJson;
import com.xuemiao.api.Json.SignInFeedbackJson;
import com.xuemiao.api.Json.SignInInfoJson;
import com.xuemiao.api.Json.SignInInfoTimeSegment;
import com.xuemiao.exception.StudentNotExistException;
import com.xuemiao.exception.UnallowedSignInTimeException;
import com.xuemiao.model.pdm.*;
import com.xuemiao.model.pdm.primaryKey.CoursePerWeekPKey;
import com.xuemiao.model.pdm.primaryKey.FingerprintPK;
import com.xuemiao.model.repository.*;
import com.xuemiao.utils.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by root on 16-10-19.
 */
@Service
public class SignInInfoService {
    @Autowired
    FingerprintRepository fingerprintRepository;
    @Autowired
    SignInInfoV2Repository signInInfoV2Repository;
    @Autowired
    SignInInfoRecordRepository signInInfoRecordRepository;
    @Autowired
    AbsencesService absencesService;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    CoursePerWeekRepository coursePerWeekRepository;
    @Autowired
    SemesterService semesterService;


    private Long checkFingerPrint(String fingerprint) {
        FingerprintPK fingerprintPK = new FingerprintPK();
        return fingerprintRepository.findByToken(fingerprint).getStudentId();
    }

    public SignInFeedbackJson getSignInFeedBackJson(String fingerprint, int statusFeedBack) {
        SignInFeedbackJson signInFeedbackJson = new SignInFeedbackJson();
        FingerprintEntity fingerprintEntity = fingerprintRepository.findByToken(fingerprint);
        StudentEntity studentEntity = studentRepository.findOne(fingerprintEntity.getStudentId());
        signInFeedbackJson.setName(studentEntity.getName());
        signInFeedbackJson.setStatusFeedBack(statusFeedBack);
        return signInFeedbackJson;
    }

    //1--previously have not signed in, 2-- previously have signed in
    public int signIn(FingerprintJson fingerprintJson) throws UnallowedSignInTimeException, StudentNotExistException {
        int statusFeedBack;
        DateTime dateTimeNow = DateTime.now();

        if (dateTimeNow.getHourOfDay() < 4) {
            throw new UnallowedSignInTimeException();
        }

        Long studentId = this.checkFingerPrint(fingerprintJson.getFingerprint());
        if (studentId == null) {
            throw new StudentNotExistException();
        }
        Date nowDate = new Date(dateTimeNow.getMillis());
        SignInInfoRecordEntity signInInfoRecordEntity;
        //find signing info of today
        SignInInfoV2Entity signInInfoV2Entity = signInInfoV2Repository.findOneByStudentIdAndDate(studentId, nowDate);
        if (signInInfoV2Entity == null) {
            signInInfoV2Entity = addSignInInfo(studentId);
            signInInfoRecordEntity = signInArrive(signInInfoV2Entity);
            statusFeedBack = 1;
        } else {
            Timestamp now = new Timestamp(dateTimeNow.getMillis());
            signInInfoRecordEntity = signInInfoRecordRepository.findOneUnfinishedSignInRecord(signInInfoV2Entity.getId());
            if (signInInfoRecordEntity == null) {
                signInInfoRecordEntity = signInArrive(signInInfoV2Entity);
                statusFeedBack = 1;
            } else {
                signInInfoRecordEntity.setEndTime(now);
                statusFeedBack = 2;
            }
        }
        signInInfoRecordRepository.save(signInInfoRecordEntity);
        return statusFeedBack;
    }

    public SignInInfoV2Entity addSignInInfo(Long studentId) {
        SignInInfoV2Entity signInInfoV2Entity = new SignInInfoV2Entity();
        signInInfoV2Entity.setStudentId(studentId);
        signInInfoV2Entity.setOperDate(new Date(DateTime.now().getMillis()));
        signInInfoV2Repository.save(signInInfoV2Entity);
        return signInInfoV2Entity;
    }

    private SignInInfoRecordEntity signInArrive(SignInInfoV2Entity signInInfoV2Entity) {
        SignInInfoRecordEntity signInInfoRecordEntity = new SignInInfoRecordEntity();
        signInInfoRecordEntity.setSignInInfoId(signInInfoV2Entity.getId());
        signInInfoRecordEntity.setStartTime(new Timestamp(DateTime.now().getMillis()));
        return signInInfoRecordEntity;
    }

    public Date getSignInInfoLatestDate() {
        return signInInfoV2Repository.getLatestSignInInfoDate();
    }

    public List<SignInInfoJson> getSignInInfoJsonsByDate(Date date) {
        List<SignInInfoV2Entity> signInInfoV2Entities = signInInfoV2Repository.findByOperDate(date);
        List<SignInInfoJson> signInInfoJsonList = new ArrayList<>();
        for (SignInInfoV2Entity signInInfoV2Entity : signInInfoV2Entities) {
            signInInfoJsonList.add(wrapSignInInfoIntoJson(signInInfoV2Entity));
        }
        return signInInfoJsonList;
    }

    public void deleteSignInInfoByStudentId(Long id) {
        List<SignInInfoV2Entity> signInInfoV2Entities = signInInfoV2Repository.findByStudentId(id);
        for (SignInInfoV2Entity signInInfoV2Entity : signInInfoV2Entities) {
            signInInfoRecordRepository.deleteBySignInInfoId(signInInfoV2Entity.getId());
            absencesService.deleteBySignInInfoId(signInInfoV2Entity.getId());
        }
        signInInfoV2Repository.deleteByStudentId(id);
    }

    //0--now in, 1--previously in, 2--course, 3--absences, 4--not in
    private SignInInfoJson wrapSignInInfoIntoJson(SignInInfoV2Entity signInInfoV2Entity) {
        DateTime dateTime = new DateTime(signInInfoV2Entity.getOperDate());

        DateTime startDate = DateUtils.date2DateTime(semesterService.checkSemesterByDate(signInInfoV2Entity.getOperDate()).getStartDate());

        int currentWeek = DateUtils.getCurrentWeek(startDate, dateTime);
        int currentWeekday = DateUtils.getCurrentWeekDay(startDate, dateTime);

        DateTime signInDate = new DateTime(signInInfoV2Entity.getOperDate());
        DateTime now = DateTime.now();

        SignInInfoJson signInInfoJson = new SignInInfoJson();
        signInInfoJson.setStudentId(signInInfoV2Entity.getStudentId().toString());
        signInInfoJson.setName(studentRepository.findOne(signInInfoV2Entity.getStudentId()).getName());
        List<SignInInfoTimeSegment> signInInfoTimeSegments = new ArrayList<>();
        List<SignInInfoRecordEntity> signInInfoRecordEntities = signInInfoRecordRepository.findBySignInInfoId(signInInfoV2Entity.getId());
        for (SignInInfoRecordEntity signInInfoRecordEntity : signInInfoRecordEntities) {
            SignInInfoTimeSegment signInInfoTimeSegment = new SignInInfoTimeSegment();
            signInInfoTimeSegment.setStartTime(DateUtils.timestamp2String(signInInfoRecordEntity.getStartTime(), 3));
            if (signInInfoRecordEntity.getEndTime() != null) {
                signInInfoTimeSegment.setEndTime(DateUtils.timestamp2String(signInInfoRecordEntity.getEndTime(), 3));
                signInInfoTimeSegment.setType(1);
            } else if (DateUtils.isToday(signInDate)) {
                signInInfoTimeSegment.setType(0);
            }
            signInInfoTimeSegment.setExtra("在实验室");
            signInInfoTimeSegments.add(signInInfoTimeSegment);
        }

        List<CourseEntity> courseEntities = courseRepository.getCoursesByStudentAndWeekAndSemesterId(signInInfoV2Entity.getStudentId(), currentWeek, semesterService.checkSemesterByDate(signInInfoV2Entity.getOperDate()).getId());
        for (CourseEntity courseEntity : courseEntities) {
            SignInInfoTimeSegment signInInfoTimeSegment = wrapCourseIntoSignInCourseInfoJson(courseEntity, currentWeekday);
            if (signInInfoTimeSegment != null) {
                signInInfoTimeSegments.add(signInInfoTimeSegment);
            }
        }

        List<AbsenceEntity> absenceEntities = absencesService.getAbsenceBySignInInfoId(signInInfoV2Entity.getId());
        for (AbsenceEntity absenceEntity : absenceEntities) {
            SignInInfoTimeSegment signInInfoTimeSegment = new SignInInfoTimeSegment();
            signInInfoTimeSegment.setStartTime(DateUtils.timestamp2String(absenceEntity.getStartAbsence(), 3));
            signInInfoTimeSegment.setEndTime(DateUtils.timestamp2String(absenceEntity.getEndAbsence(), 3));
            if (absenceEntity.isMakeUp()){
                signInInfoTimeSegment.setExtra("补签：" + absenceEntity.getAbsenceReason());
            }
            else{
                signInInfoTimeSegment.setExtra("请假：" + absenceEntity.getAbsenceReason());
            }
            signInInfoTimeSegment.setType(3);
            signInInfoTimeSegments.add(signInInfoTimeSegment);
        }

        Collections.sort(signInInfoTimeSegments);

        if (signInInfoTimeSegments.size() == 0) {
            SignInInfoTimeSegment signInInfoTimeSegment = new SignInInfoTimeSegment();
            signInInfoTimeSegment.setStartTime("00:00");
            signInInfoTimeSegment.setType(4);
            if (DateUtils.isToday(signInDate)) {
                signInInfoTimeSegment.setEndTime(DateUtils.getNowHourMinuteStr());
            } else {
                signInInfoTimeSegment.setEndTime("23:59");
            }
            signInInfoTimeSegment.setExtra("不在实验室");
            signInInfoTimeSegments.add(signInInfoTimeSegment);
        } else {
            SignInInfoTimeSegment signInInfoTimeSegmentFront = signInInfoTimeSegments.get(0);
            for (int i = 1; i < signInInfoTimeSegments.size(); i++) {
                if (signInInfoTimeSegmentFront.getEndTime() == null) {
                    Iterator<SignInInfoTimeSegment> signInInfoTimeSegmentIterator = signInInfoTimeSegments.
                            subList(i, signInInfoTimeSegments.size() - 1).iterator();
                    while (signInInfoTimeSegmentIterator.hasNext()) {
                        SignInInfoTimeSegment signInInfoTimeSegmentTemp = signInInfoTimeSegmentIterator.next();
                        if (DateUtils.timestamp2String(new Timestamp(now.getMillis()), 3).compareTo(signInInfoTimeSegmentTemp.getStartTime()) >= 0) {
                            signInInfoTimeSegments.remove(signInInfoTimeSegments.indexOf(signInInfoTimeSegmentTemp));
                        }
                    }
                } else {
                    SignInInfoTimeSegment signInInfoTimeSegmentBack = signInInfoTimeSegments.get(i);
                    if (signInInfoTimeSegmentFront.getEndTime().compareTo(signInInfoTimeSegmentBack.getStartTime()) >= 0) {
                        signInInfoTimeSegmentFront.setEndTime(signInInfoTimeSegmentBack.getStartTime());
                        signInInfoTimeSegments.set(i - 1, signInInfoTimeSegmentFront);
                    }
                }
                signInInfoTimeSegmentFront = signInInfoTimeSegments.get(i);
            }

            List<SignInInfoTimeSegment> signInInfoTimeSegmentsTemp = new ArrayList<>();

            String startTimeTemp = signInInfoTimeSegments.get(0).getStartTime();
            String endTimeTemp;

            SignInInfoTimeSegment signInInfoTimeSegment = new SignInInfoTimeSegment();
            if ((DateUtils.isTimeBeforeNow(startTimeTemp)) || (!DateUtils.isToday(signInDate))) {
                signInInfoTimeSegment.setStartTime("00:00");
                signInInfoTimeSegment.setEndTime(startTimeTemp);
                signInInfoTimeSegment.setType(4);
                signInInfoTimeSegment.setExtra("不在实验室");
                signInInfoTimeSegmentsTemp.add(signInInfoTimeSegment);
            } else if (!DateUtils.isTimeBeforeNow(startTimeTemp)) {
                signInInfoTimeSegment.setStartTime("00:00");
                signInInfoTimeSegment.setEndTime(DateUtils.getNowHourMinuteStr());
                signInInfoTimeSegment.setType(4);
                signInInfoTimeSegment.setExtra("不在实验室");
                signInInfoTimeSegmentsTemp.add(signInInfoTimeSegment);

                signInInfoTimeSegment = new SignInInfoTimeSegment();
                signInInfoTimeSegment.setStartTime(DateUtils.getNowHourMinuteStr());
                signInInfoTimeSegment.setEndTime(startTimeTemp);
                signInInfoTimeSegment.setType(5);
                signInInfoTimeSegmentsTemp.add(signInInfoTimeSegment);
            }

            for (int i = 1; i < signInInfoTimeSegments.size(); i++) {
                SignInInfoTimeSegment signInInfoTimeSegmentTest = signInInfoTimeSegments.get(i - 1);
                startTimeTemp = signInInfoTimeSegmentTest.getEndTime();
                if (startTimeTemp == null) {
                    startTimeTemp = DateUtils.getNowHourMinuteStr();
                    signInInfoTimeSegmentTest.setEndTime(startTimeTemp);
                    signInInfoTimeSegments.set(i - 1, signInInfoTimeSegmentTest);
                }
                endTimeTemp = signInInfoTimeSegments.get(i).getStartTime();
                if (DateUtils.isToday(signInDate)) {
                    if (DateUtils.isTimeBeforeNow(startTimeTemp) && DateUtils.isTimeBeforeNow(endTimeTemp)) {
                        signInInfoTimeSegment = new SignInInfoTimeSegment();
                        signInInfoTimeSegment.setStartTime(startTimeTemp);
                        signInInfoTimeSegment.setEndTime(endTimeTemp);
                        signInInfoTimeSegment.setType(4);
                        signInInfoTimeSegment.setExtra("不在实验室");
                        signInInfoTimeSegmentsTemp.add(signInInfoTimeSegment);
                    } else if (DateUtils.isTimeBeforeNow(startTimeTemp) && !DateUtils.isTimeBeforeNow(endTimeTemp)) {
                        signInInfoTimeSegment = new SignInInfoTimeSegment();
                        signInInfoTimeSegment.setStartTime(startTimeTemp);
                        signInInfoTimeSegment.setEndTime(DateUtils.getNowHourMinuteStr());
                        signInInfoTimeSegment.setType(4);
                        signInInfoTimeSegment.setExtra("不在实验室");
                        signInInfoTimeSegmentsTemp.add(signInInfoTimeSegment);

                        signInInfoTimeSegment = new SignInInfoTimeSegment();
                        signInInfoTimeSegment.setStartTime(DateUtils.getNowHourMinuteStr());
                        signInInfoTimeSegment.setEndTime(endTimeTemp);
                        signInInfoTimeSegment.setType(5);
                        signInInfoTimeSegmentsTemp.add(signInInfoTimeSegment);
                    } else {
                        signInInfoTimeSegment = new SignInInfoTimeSegment();
                        signInInfoTimeSegment.setStartTime(startTimeTemp);
                        signInInfoTimeSegment.setEndTime(endTimeTemp);
                        signInInfoTimeSegment.setType(5);
                        signInInfoTimeSegmentsTemp.add(signInInfoTimeSegment);
                    }
                } else {
                    signInInfoTimeSegment = new SignInInfoTimeSegment();
                    signInInfoTimeSegment.setStartTime(startTimeTemp);
                    signInInfoTimeSegment.setEndTime(endTimeTemp);
                    signInInfoTimeSegment.setType(4);
                    signInInfoTimeSegment.setExtra("不在实验室");
                    signInInfoTimeSegmentsTemp.add(signInInfoTimeSegment);
                }
            }

            SignInInfoTimeSegment tailTimeSegment = signInInfoTimeSegments.get(signInInfoTimeSegments.size() - 1);
            if (DateUtils.isToday(signInDate)) {
                endTimeTemp = tailTimeSegment.getEndTime();
                if (endTimeTemp == null) {
                    tailTimeSegment.setEndTime(DateUtils.getNowHourMinuteStr());
                    signInInfoTimeSegments.set(signInInfoTimeSegments.size() - 1, tailTimeSegment);
                } else if (DateUtils.isTimeBeforeNow(endTimeTemp)) {
                    signInInfoTimeSegment = new SignInInfoTimeSegment();
                    signInInfoTimeSegment.setStartTime(tailTimeSegment.getEndTime());
                    signInInfoTimeSegment.setType(4);
                    signInInfoTimeSegment.setEndTime(DateUtils.getNowHourMinuteStr());
                    signInInfoTimeSegment.setExtra("不在实验室");
                    signInInfoTimeSegmentsTemp.add(signInInfoTimeSegment);
                }
            } else {
                if (tailTimeSegment.getEndTime() != null) {
                    signInInfoTimeSegment = new SignInInfoTimeSegment();
                    signInInfoTimeSegment.setStartTime(tailTimeSegment.getEndTime());
                    signInInfoTimeSegment.setType(4);
                    signInInfoTimeSegment.setEndTime("23:59");
                    signInInfoTimeSegment.setExtra("不在实验室");
                    signInInfoTimeSegmentsTemp.add(signInInfoTimeSegment);
                } else {
                    tailTimeSegment.setEndTime("23:59");
                    signInInfoTimeSegments.set(signInInfoTimeSegments.size() - 1, tailTimeSegment);
                }
            }

            signInInfoTimeSegments.addAll(signInInfoTimeSegmentsTemp);
        }

        Collections.sort(signInInfoTimeSegments);

        signInInfoJson.setSignInInfoTimeSegments(signInInfoTimeSegments);
        return signInInfoJson;
    }

    private SignInInfoTimeSegment wrapCourseIntoSignInCourseInfoJson(CourseEntity courseEntity, int currentWeekday) {
        CoursePerWeekPKey coursePerWeekPKey = new CoursePerWeekPKey();
        coursePerWeekPKey.setCourseId(courseEntity.getId());
        coursePerWeekPKey.setWeekday(currentWeekday);
        CoursePerWeekEntity coursePerWeekEntity = coursePerWeekRepository.findOne(coursePerWeekPKey);
        if (coursePerWeekEntity != null) {
            SignInInfoTimeSegment signInInfoTimeSegment = new SignInInfoTimeSegment();
            signInInfoTimeSegment.setStartTime(getCourseTime(coursePerWeekEntity.getStartSection(), 1));
            signInInfoTimeSegment.setEndTime(getCourseTime(coursePerWeekEntity.getEndSection(), 2));
            signInInfoTimeSegment.setType(2);
            signInInfoTimeSegment.setExtra("上课：" + courseEntity.getCourseName());
            return signInInfoTimeSegment;
        }
        return null;
    }

    private String getCourseTime(int section, int type) {
        String courseTime = null;
        if (type == 1) {
            switch (section) {
                case 1:
                    courseTime = "08:50";
                    break;
                case 2:
                    courseTime = "09:40";
                    break;
                case 3:
                    courseTime = "10:40";
                    break;
                case 4:
                    courseTime = "11:30";
                    break;
                case 5:
                    courseTime = "14:00";
                    break;
                case 6:
                    courseTime = "14:50";
                    break;
                case 7:
                    courseTime = "15:40";
                    break;
                case 8:
                    courseTime = "16:30";
                    break;
                case 9:
                    courseTime = "17:20";
                    break;
                case 10:
                    courseTime = "19:00";
                    break;
                case 11:
                    courseTime = "19:50";
                    break;
                case 12:
                    courseTime = "20:40";
                    break;
                default:
                    courseTime = "";
            }
            return courseTime;
        } else if (type == 2) {
            switch (section) {
                case 1:
                    courseTime = "09:35";
                    break;
                case 2:
                    courseTime = "10:25";
                    break;
                case 3:
                    courseTime = "11:25";
                    break;
                case 4:
                    courseTime = "12:15";
                    break;
                case 5:
                    courseTime = "14:45";
                    break;
                case 6:
                    courseTime = "15:35";
                    break;
                case 7:
                    courseTime = "16:25";
                    break;
                case 8:
                    courseTime = "17:15";
                    break;
                case 9:
                    courseTime = "18:05";
                    break;
                case 10:
                    courseTime = "19:45";
                    break;
                case 11:
                    courseTime = "20:35";
                    break;
                case 12:
                    courseTime = "21:25";
                    break;
                default:
                    courseTime = "";
            }
        }
        return courseTime;
    }

}

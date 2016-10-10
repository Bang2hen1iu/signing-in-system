package com.xuemiao.api;

import com.xuemiao.api.Json.*;
import com.xuemiao.exception.DateFormatErrorException;
import com.xuemiao.exception.ImgNotExistException;
import com.xuemiao.model.pdm.*;
import com.xuemiao.model.repository.*;
import com.xuemiao.service.StatisticsService;
import com.xuemiao.utils.DateUtils;
import com.xuemiao.utils.PasswordUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dzj on 10/1/2016.
 */
@Component
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
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    CoursePerWeekRepository coursePerWeekRepository;
    @Autowired
    StatisticsService statisticsService;
    @Value("${signatureImgPath}")
    String signatureImgPath;
    @Value("${course.start_date}")
    String courseStartDateString;

    @GET
    @Path("/{psw}")
    public String getPswHash(@PathParam("psw") String psw) {
        return PasswordUtils.createPasswordHash(psw);
    }

    @GET
    @Path("/students")
    public Response getStudents() {
        List<StudentEntity> studentEntities = studentRepository.findAll();
        return Response.ok().entity(studentEntities).build();
    }

    @GET
    @Path("/statistics/{date}")
    public Response getStatisticData(@PathParam("date") Date date) {
        List<StatisticsEntity> statisticsEntities = statisticsRepository.findByOperDate(date);
        return Response.ok().entity(statisticsEntities).build();
    }

    @GET
    @Path("/statistics/sum")
    public Response getStatisticsSum() {
        DateTime now = DateTime.now();
        DateTime dateTime = new DateTime(now.getYear(), now.getMonthOfYear(), 1, 0, 0, 0, 0);
        List<Object[]> statisticList = statisticsRepository.getStatisticsByStartDate(new Date(dateTime.getMillis()));
        return Response.ok().entity(statisticsService.object2Json(statisticList)).build();
    }

    @GET
    @Path("/courses/{studentId}")
    public Response getCourseByStudentId(@PathParam("studentId") Long studentId) {
        List<CoursesInfoJson> coursesInfoJsonList = new ArrayList<>();
        List<CourseEntity> courseEntities = courseRepository.findByStudentId(studentId);
        for (CourseEntity courseEntity : courseEntities) {
            CoursesInfoJson coursesInfoJson = new CoursesInfoJson();
            coursesInfoJson.setStudentId(courseEntity.getStudentId());
            coursesInfoJson.setCourseName(courseEntity.getCourseName());
            coursesInfoJson.setStartWeek(courseEntity.getStartWeek());
            coursesInfoJson.setEndWeek(courseEntity.getEndWeek());
            List<CoursePerWeekJson> coursePerWeekJsonList = new ArrayList<>();
            List<CoursePerWeekEntity> coursePerWeekEntities = coursePerWeekRepository.findByIdAndName(
                    courseEntity.getStudentId(), courseEntity.getCourseName());
            for (CoursePerWeekEntity coursePerWeekEntity : coursePerWeekEntities) {
                CoursePerWeekJson coursePerWeekJson = new CoursePerWeekJson();
                coursePerWeekJson.setWeekday(coursePerWeekEntity.getWeekday());
                coursePerWeekJson.setStartSection(coursePerWeekEntity.getStartSection());
                coursePerWeekJson.setEndSection(coursePerWeekEntity.getEndSection());
                coursePerWeekJsonList.add(coursePerWeekJson);
            }
            coursesInfoJson.setCoursePerWeekJsonList(coursePerWeekJsonList);
            coursesInfoJsonList.add(coursesInfoJson);
        }
        System.out.println(coursesInfoJsonList);
        return Response.ok().entity(coursesInfoJsonList).build();
    }

    @GET
    @Path("/duty_students/{date}")
    public Response getDutyStudents(@PathParam("date") Date date)
            throws DateFormatErrorException {
        List<DutyStudentEntity> dutyStudentEntities = dutyStudentRepository.findByOperDate(date);
        List<DutyStudentJson> dutyStudentJsonList = new ArrayList<>();
        for (DutyStudentEntity dutyStudentEntity : dutyStudentEntities) {
            DutyStudentJson dutyStudentJson = new DutyStudentJson();
            dutyStudentJson.setStudentId(dutyStudentEntity.getStudentId());
            dutyStudentJson.setName(studentRepository.findOne(dutyStudentEntity.getStudentId()).getName());
            dutyStudentJson.setStartDate(dutyStudentEntity.getStartDate());
            dutyStudentJson.setEndDate(dutyStudentEntity.getEndDate());
            dutyStudentJsonList.add(dutyStudentJson);
        }
        return Response.ok().entity(dutyStudentJsonList).build();
    }

    @GET
    @Path("/sign_in_info/{date}")
    public Response getSignInInfo(@PathParam("date") Date date) {
        DateTime dateTime = new DateTime(date);

        List<SignInInfoEntity> signInInfoEntities = signInInfoRepository.findByOperDate(new Date(dateTime.getMillis()));
        List<SignInInfoJson> signInInfoJsonList = new ArrayList<>();
        AbsenceEntity absenceEntity;
        for (SignInInfoEntity signInInfoEntity : signInInfoEntities) {
            SignInInfoJson signInInfoJson = new SignInInfoJson();
            signInInfoJson.setStudentId(signInInfoEntity.getStudentId().toString());
            signInInfoJson.setName(studentRepository.findOne(signInInfoEntity.getStudentId()).getName());
            if (signInInfoEntity.getStartMorning() != null) {
                signInInfoJson.setStartMorning(signInInfoEntity.getStartMorning());
                signInInfoJson.setStartMorningSignatureImgName(signInInfoEntity.getStartMorningSignatureImgName());
            }
            if (signInInfoEntity.getEndMorning() != null) {
                signInInfoJson.setEndMorning(signInInfoEntity.getEndMorning());
                signInInfoJson.setEndMorningSignatureImgName(signInInfoEntity.getEndMorningSignatureImgName());
            }
            if (signInInfoEntity.getStartAfternoon() != null) {
                signInInfoJson.setStartAfternoon(signInInfoEntity.getStartAfternoon());
                signInInfoJson.setStartAfternoonSignatureImgName(signInInfoEntity.getStartAfternoonSignatureImgName());
            }
            if (signInInfoEntity.getEndAfternoon() != null) {
                signInInfoJson.setEndAfternoon(signInInfoEntity.getEndAfternoon());
                signInInfoJson.setEndAfternoonSignatureImgName(signInInfoEntity.getEndAfternoonSignatureImgName());
            }
            if (signInInfoEntity.getStartNight() != null) {
                signInInfoJson.setStartNight(signInInfoEntity.getStartNight());
                signInInfoJson.setStartNightSignatureImgName(signInInfoEntity.getStartNightSignatureImgName());
            }
            if (signInInfoEntity.getEndNight() != null) {
                signInInfoJson.setEndNight(signInInfoEntity.getEndNight());
                signInInfoJson.setEndNightSignatureImgName(signInInfoEntity.getEndNightSignatureImgName());
            }

            List<SignInInfoCoursesInfo> signInInfoCoursesInfoList = new ArrayList<>();
            DateTime startDate = DateTime.parse(courseStartDateString);
            int currentWeek = DateUtils.getCurrentWeek(startDate,dateTime);
            int currentWeekday = DateUtils.getCurrentWeekDay(startDate,dateTime);
            List<CourseEntity> courseEntities = courseRepository.getCoursesByStudentAndWeek(signInInfoEntity.getStudentId(), currentWeek);
            CoursePerWeekEntity coursePerWeekEntity;
            for (CourseEntity courseEntity : courseEntities) {
                coursePerWeekEntity = coursePerWeekRepository.findOneByIdAndNameAndWeekday(
                        courseEntity.getStudentId(), courseEntity.getCourseName(), currentWeekday);
                if (coursePerWeekEntity != null) {
                    SignInInfoCoursesInfo signInInfoCoursesInfo = new SignInInfoCoursesInfo();
                    signInInfoCoursesInfo.setCourseName(courseEntity.getCourseName());
                    signInInfoCoursesInfo.setStartSection(coursePerWeekEntity.getStartSection());
                    signInInfoCoursesInfo.setEndSection(coursePerWeekEntity.getEndSection());
                    signInInfoCoursesInfoList.add(signInInfoCoursesInfo);
                }
            }
            signInInfoJson.setSignInInfoCoursesInfoList(signInInfoCoursesInfoList);

            StudentIdAndOperDateKey studentIdAndOperDateKey = new StudentIdAndOperDateKey();
            studentIdAndOperDateKey.setStudentId(signInInfoEntity.getStudentId());
            studentIdAndOperDateKey.setOperDate(signInInfoEntity.getOperDate());
            absenceEntity = absenceRepository.findOne(studentIdAndOperDateKey);
            if (absenceEntity != null) {
                signInInfoJson.setStartAbsence(absenceEntity.getStartAbsence());
                signInInfoJson.setEndAbsence(absenceEntity.getEndAbsence());
                signInInfoJson.setAbsenceReason(absenceEntity.getAbsenceReason());
            }

            signInInfoJsonList.add(signInInfoJson);
        }
        return Response.ok().entity(signInInfoJsonList).build();
    }

    @GET
    @Path("/sign_in_info/signatures/{name}")
    public Response getSignatureImgByPath(@PathParam("name") String name)
            throws ImgNotExistException {
        File f = new File(signatureImgPath + name);
        if (!f.exists()) {
            throw new ImgNotExistException();
        }
        String mt = new MimetypesFileTypeMap().getContentType(f);
        return Response.ok(f, mt).build();
    }

    @GET
    @Path("/absences/{studentId}/{operDate}")
    public Response getStudentAbsence(@PathParam("studentId") Long studentId,
                                      @PathParam("operDate") String operDate) {
        StudentIdAndOperDateKey studentIdAndOperDateKey = new StudentIdAndOperDateKey();
        studentIdAndOperDateKey.setStudentId(studentId);
        studentIdAndOperDateKey.setOperDate(new Date(DateUtils.parseDateString(operDate).getMillis()));
        AbsenceEntity absenceEntity = absenceRepository.findOne(studentIdAndOperDateKey);
        return Response.ok().entity(absenceEntity).build();
    }

    @GET
    @Path("/sign_in_info/date")
    public Response getSignInInfoDate() {
        List<Date> dateList = signInInfoRepository.getAllSignInInfoDate();
        return Response.ok().entity(DateUtils.DateList2DateStringList(dateList)).build();
    }

    private class DateJson{
        private Date date;
        private String weekday;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getWeekday() {
            return weekday;
        }

        public void setWeekday(String weekday) {
            this.weekday = weekday;
        }
    }

    @GET
    @Path("/sign_in_info/latest_date")
    public Response getSignInInfoLatestDate() {
        DateJson dateJson = new DateJson();
        Date date = signInInfoRepository.getLatestSignInInfoDate();
        dateJson.setDate(date);
        String weekday;
        switch (DateUtils.getCurrentWeekDay(DateTime.parse(courseStartDateString),new DateTime(date.getTime()))){
            case 1:
                weekday = "星期一";
                break;
            case 2:
                weekday = "星期二";
                break;
            case 3:
                weekday = "星期三";
                break;
            case 4:
                weekday = "星期四";
                break;
            case 5:
                weekday = "星期五";
                break;
            case 6:
                weekday = "星期六";
                break;
            case 7:
                weekday = "星期日";
                break;
            default:
                weekday = "";
        }
        dateJson.setWeekday(weekday);
        return Response.ok().entity(dateJson).build();
    }

    @GET
    @Path("/system_time")
    public Response getSystemTime(){
        DateTime now = DateTime.now();
        return Response.ok().entity(now.getYear()+"-"+String.format("%02d", now.getMonthOfYear())+"-"+String.format("%02d", now.getDayOfMonth())+" "+String.format("%02d", now.getHourOfDay())+":"+String.format("%02d", now.getMinuteOfHour())+":"+String.format("%02d", now.getSecondOfMinute())).build();
    }

    @GET
    @Path("/statistics/date")
    public Response getStatisticsDate() {
        List<Date> dateList = statisticsRepository.getAllStatisticsDate();
        return Response.ok().entity(DateUtils.DateList2DateStringList(dateList)).build();
    }

    @GET
    @Path("/statistics/latest_date")
    public Response getStatisticsLatestDate() {
        Date date = statisticsRepository.getLatestStatisticsDate();
        return Response.ok().entity(date).build();
    }

}

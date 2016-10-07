package com.xuemiao.api;

import com.xuemiao.api.Json.CoursePerWeekJson;
import com.xuemiao.api.Json.CoursesInfoJson;
import com.xuemiao.api.Json.DutyStudentJson;
import com.xuemiao.api.Json.SignInInfoJson;
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
    public Response getSignInInfo(@PathParam("date") Date date)
            throws DateFormatErrorException {
        DateTime dateTime = new DateTime(date);
        if (dateTime == null) {
            throw new DateFormatErrorException();
        }
        List<SignInInfoEntity> signInInfoEntities = signInInfoRepository.findByOperDate(new Date(dateTime.getMillis()));
        List<SignInInfoJson> signInInfoJsonList = new ArrayList<>();
        AbsenceEntity absenceEntity;
        for (SignInInfoEntity signInInfoEntity : signInInfoEntities) {
            SignInInfoJson signInInfoJson = new SignInInfoJson();
            signInInfoJson.setStudentId(signInInfoEntity.getStudentId().toString());
            signInInfoJson.setName(studentRepository.findOne(signInInfoEntity.getStudentId()).getName());
            if (signInInfoEntity.getStartMorning() != null) {
                signInInfoJson.setStartMorning(signInInfoEntity.getStartMorning());
            }
            if (signInInfoEntity.getEndMorning() != null) {
                signInInfoJson.setEndMorning(signInInfoEntity.getEndMorning());
            }
            if (signInInfoEntity.getStartAfternoon() != null) {
                signInInfoJson.setStartAfternoon(signInInfoEntity.getStartAfternoon());
            }
            if (signInInfoEntity.getEndAfternoon() != null) {
                signInInfoJson.setEndAfternoon(signInInfoEntity.getEndAfternoon());
            }
            if (signInInfoEntity.getStartNight() != null) {
                signInInfoJson.setStartNight(signInInfoEntity.getStartNight());
            }
            if (signInInfoEntity.getEndNight() != null) {
                signInInfoJson.setEndNight(signInInfoEntity.getEndNight());
            }
            signInInfoJson.setCurrentDayCourses(signInInfoEntity.getCurrentDayCourses());

            StudentIdAndOperDateKey studentIdAndOperDateKey = new StudentIdAndOperDateKey();
            studentIdAndOperDateKey.setStudentId(signInInfoEntity.getStudentId());
            studentIdAndOperDateKey.setOperDate(signInInfoEntity.getOperDate());
            absenceEntity = absenceRepository.findOne(studentIdAndOperDateKey);
            if (absenceEntity != null) {
                signInInfoJson.setStartAbsence(absenceEntity.getStartAbsence());
                signInInfoJson.setEndAbsence(absenceEntity.getEndAbsence());
                signInInfoJson.setAbsenceReason(absenceEntity.getAbsenceReason());
            }

            signInInfoJson.setStartMorningSignatureImgName(signInInfoEntity.getStartMorningSignatureImgName());
            signInInfoJson.setEndMorningSignatureImgName(signInInfoEntity.getEndMorningSignatureImgName());
            signInInfoJson.setStartAfternoonSignatureImgName(signInInfoEntity.getStartAfternoonSignatureImgName());
            signInInfoJson.setEndAfternoonSignatureImgName(signInInfoEntity.getEndAfternoonSignatureImgName());
            signInInfoJson.setStartNightSignatureImgName(signInInfoEntity.getStartNightSignatureImgName());
            signInInfoJson.setEndNightSignatureImgName(signInInfoEntity.getEndNightSignatureImgName());
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

    @GET
    @Path("/sign_in_info/latest_date")
    public Response getSignInInfoLatestDate() {
        Date date = signInInfoRepository.getLatestSignInInfoDate();
        return Response.ok().entity(date).build();
    }

    @GET
    @Path("/system_time")
    public Response getSystemTime(){
        DateTime now = DateTime.now();
        return Response.ok().entity(now.getHourOfDay()+":"+now.getMinuteOfHour()+":"+now.getSecondOfMinute()).build();
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

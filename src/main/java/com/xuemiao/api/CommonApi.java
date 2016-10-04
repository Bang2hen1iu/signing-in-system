package com.xuemiao.api;

import com.xuemiao.api.Json.CoursePerWeekJson;
import com.xuemiao.api.Json.CoursesInfoJson;
import com.xuemiao.api.Json.SignInInfoJson;
import com.xuemiao.api.Json.StatisticJson;
import com.xuemiao.exception.DateFormatErrorException;
import com.xuemiao.model.pdm.*;
import com.xuemiao.model.repository.*;
import com.xuemiao.utils.DateUtils;
import com.xuemiao.utils.PasswordUtils;
import com.xuemiao.utils.PrecisionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dzj on 10/1/2016.
 */
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
    public Response getStatisticData(@PathParam("date") String dateString) {
        DateTime date = DateUtils.parseDateString(dateString);
        List<StatisticsEntity> statisticsEntities = statisticsRepository.findByOperDate(date);
        return Response.ok().entity(statisticsEntities).build();
    }

    @GET
    @Path("/statistics/sum")
    public Response getStatisticsSum() {
        List<StatisticJson> statisticJsonList = new ArrayList<>();
        List<Object[]> statisticList = statisticsRepository.getStatisticsSum();
        for (Object[] statistic : statisticList) {
            StatisticJson statisticJson = new StatisticJson();
            statisticJson.setId((Long) statistic[0]);
            statisticJson.setStayLabTime(PrecisionUtils.transferToSecondDecimal((double) statistic[1]));
            statisticJson.setAbsenceTimes((Long) statistic[2]);
            statisticJson.setName(studentRepository.findOne((Long) statistic[0]).getName());
            statisticJsonList.add(statisticJson);
        }
        return Response.ok().entity(statisticJsonList).build();
    }

    @GET
    @Path("/courses/{studentId}")
    public Response getCourseByStudentId(@PathParam("studentId")Long studentId){
        List<CoursesInfoJson> coursesInfoJsonList = new ArrayList<>();
        List<CourseEntity> courseEntities = courseRepository.findByStudentId(studentId);
        for (CourseEntity courseEntity : courseEntities){
            CoursesInfoJson coursesInfoJson = new CoursesInfoJson();
            coursesInfoJson.setStudentId(courseEntity.getStudentId());
            coursesInfoJson.setCourseName(courseEntity.getCourseName());
            coursesInfoJson.setStartWeek(courseEntity.getStartWeek());
            coursesInfoJson.setEndWeek(courseEntity.getEndWeek());
            List<CoursePerWeekJson> coursePerWeekJsonList = new ArrayList<>();
            List<CoursePerWeekEntity> coursePerWeekEntities = coursePerWeekRepository.findByIdAndName(
                    courseEntity.getStudentId(),courseEntity.getCourseName());
            for (CoursePerWeekEntity coursePerWeekEntity : coursePerWeekEntities){
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
    public Response getDutyStudents(@PathParam("date") String dateString)
            throws DateFormatErrorException {
        DateTime date = DateUtils.parseDateString(dateString);
        if (date == null) {
            throw new DateFormatErrorException();
        }
        List<DutyStudentEntity> dutyStudentEntities = dutyStudentRepository.findByOperDate(new Date(date.getMillis()));
        return Response.ok().entity(dutyStudentEntities).build();
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
        for (SignInInfoEntity signInInfoEntity : signInInfoEntities){
            SignInInfoJson signInInfoJson = new SignInInfoJson();
            signInInfoJson.setStudentId(signInInfoEntity.getStudentId().toString());
            signInInfoJson.setName(studentRepository.findOne(signInInfoEntity.getStudentId()).getName());
            if(signInInfoEntity.getStartMorning()!=null){
                signInInfoJson.setStartMorning(signInInfoEntity.getStartMorning());
            }
            if(signInInfoEntity.getEndMorning()!=null){
                signInInfoJson.setEndMorning(signInInfoEntity.getEndMorning());
            }
            if(signInInfoEntity.getStartAfternoon()!=null){
                signInInfoJson.setStartAfternoon(signInInfoEntity.getStartAfternoon());
            }
            if(signInInfoEntity.getEndAfternoon()!=null){
                signInInfoJson.setEndAfternoon(signInInfoEntity.getEndAfternoon());
            }
            if(signInInfoEntity.getStartNight()!=null){
                signInInfoJson.setStartNight(signInInfoEntity.getStartNight());
            }
            if(signInInfoEntity.getEndNight()!=null){
                signInInfoJson.setEndNight(signInInfoEntity.getEndNight());
            }
            signInInfoJson.setCurrentDayCourses(signInInfoEntity.getCurrentDayCourses());

            StudentIdAndOperDateKey studentIdAndOperDateKey = new StudentIdAndOperDateKey();
            studentIdAndOperDateKey.setStudentId(signInInfoEntity.getStudentId());
            studentIdAndOperDateKey.setOperDate(signInInfoEntity.getOperDate());
            absenceEntity = absenceRepository.findOne(studentIdAndOperDateKey);
            if(absenceEntity!=null){
                signInInfoJson.setStartAbsence(absenceEntity.getStartAbsence());
                signInInfoJson.setEndAbsence(absenceEntity.getEndAbsence());
                signInInfoJson.setAbsenceReason(absenceEntity.getAbsenceReason());
            }
            signInInfoJsonList.add(signInInfoJson);
        }
        return Response.ok().entity(signInInfoJsonList).build();
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

package com.xuemiao.service;

import com.xuemiao.api.Json.CoursePerWeekJson;
import com.xuemiao.api.Json.CoursesInfoJson;
import com.xuemiao.model.pdm.CourseEntity;
import com.xuemiao.model.pdm.CoursePerWeekEntity;
import com.xuemiao.model.repository.CoursePerWeekRepository;
import com.xuemiao.model.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16-10-19.
 */
@Service
public class CoursesService {
    @Autowired
    CoursePerWeekRepository coursePerWeekRepository;
    @Autowired
    CourseRepository courseRepository;

    public void saveCoursePerWeekJson(Long courseId, CoursePerWeekJson coursePerWeekJson) {
        CoursePerWeekEntity coursePerWeekEntity = new CoursePerWeekEntity();
        coursePerWeekEntity.setCourseId(courseId);
        coursePerWeekEntity.setStartSection(coursePerWeekJson.getStartSection());
        coursePerWeekEntity.setEndSection(coursePerWeekJson.getEndSection());
        coursePerWeekEntity.setWeekday(coursePerWeekJson.getWeekday());
        coursePerWeekRepository.save(coursePerWeekEntity);
    }

    public List<CoursesInfoJson> getCoursesJsonsByStudentId(Long studentId) {
        List<CoursesInfoJson> coursesInfoJsonList = new ArrayList<>();
        List<CourseEntity> courseEntities = courseRepository.findByStudentId(studentId);
        for (CourseEntity courseEntity : courseEntities) {
            coursesInfoJsonList.add(wrapCourseIntoJson(courseEntity));
        }
        return coursesInfoJsonList;
    }

    public void addCourse(CoursesInfoJson coursesInfoJson) {
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setStudentId(coursesInfoJson.getStudentId());
        courseEntity.setCourseName(coursesInfoJson.getCourseName());
        courseEntity.setStartWeek(coursesInfoJson.getStartWeek());
        courseEntity.setEndWeek(coursesInfoJson.getEndWeek());
        for (CoursePerWeekJson coursePerWeekJson : coursesInfoJson.getCoursePerWeekJsonList()) {
            if (coursePerWeekJson == null) {
                continue;
            }
            this.saveCoursePerWeekJson(courseEntity.getId(), coursePerWeekJson);
        }
    }

    public void modifyCourse(CoursesInfoJson coursesInfoJson) {
        CourseEntity courseEntity = courseRepository.findOne(coursesInfoJson.getId());
        courseEntity.setStartWeek(coursesInfoJson.getStartWeek());
        courseEntity.setEndWeek(coursesInfoJson.getEndWeek());
        courseRepository.save(courseEntity);
        coursePerWeekRepository.deleteByCourseId(coursesInfoJson.getId());
        for (CoursePerWeekJson coursePerWeekJson : coursesInfoJson.getCoursePerWeekJsonList()) {
            this.saveCoursePerWeekJson(coursesInfoJson.getId(), coursePerWeekJson);
        }
    }

    public void deleteCourseByCourseId(Long id) {
        coursePerWeekRepository.deleteByCourseId(id);
        courseRepository.delete(id);
    }

    public void deleteCourseByStudentId(Long id) {
        List<CourseEntity> courseEntities = courseRepository.findByStudentId(id);
        for (CourseEntity courseEntity : courseEntities) {
            coursePerWeekRepository.deleteByCourseId(courseEntity.getId());
        }
        courseRepository.deleteByStudentId(id);
    }

    private CoursesInfoJson wrapCourseIntoJson(CourseEntity courseEntity) {
        CoursesInfoJson coursesInfoJson = new CoursesInfoJson();
        coursesInfoJson.setStudentId(courseEntity.getStudentId());
        coursesInfoJson.setCourseName(courseEntity.getCourseName());
        coursesInfoJson.setStartWeek(courseEntity.getStartWeek());
        coursesInfoJson.setEndWeek(courseEntity.getEndWeek());
        List<CoursePerWeekJson> coursePerWeekJsonList = new ArrayList<>();
        List<CoursePerWeekEntity> coursePerWeekEntities = coursePerWeekRepository.findByCourseId(
                courseEntity.getId());
        for (CoursePerWeekEntity coursePerWeekEntity : coursePerWeekEntities) {
            coursePerWeekJsonList.add(wrapCoursePerWeekIntoJson(coursePerWeekEntity));
        }
        coursesInfoJson.setCoursePerWeekJsonList(coursePerWeekJsonList);
        return coursesInfoJson;
    }

    private CoursePerWeekJson wrapCoursePerWeekIntoJson(CoursePerWeekEntity coursePerWeekEntity) {
        CoursePerWeekJson coursePerWeekJson = new CoursePerWeekJson();
        coursePerWeekJson.setWeekday(coursePerWeekEntity.getWeekday());
        coursePerWeekJson.setStartSection(coursePerWeekEntity.getStartSection());
        coursePerWeekJson.setEndSection(coursePerWeekEntity.getEndSection());
        return coursePerWeekJson;
    }
}

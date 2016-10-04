package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.CoursePerWeekEntity;
import com.xuemiao.model.pdm.StudentAndCourseNameKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by dzj on 10/4/2016.
 */
public interface CoursePerWeekRepository extends JpaRepository<CoursePerWeekEntity, StudentAndCourseNameKey>{
    @Query("select c from CoursePerWeekEntity c where c.studentId = :studentId and c.courseName = :courseName and c.weekDay = :weekDay")
    CoursePerWeekEntity findOneByIdAndNameAndWeekday(@Param("studentId")Long studentId,
                                                     @Param("courseName")String courseName,
                                                     @Param("weekDay")int weekDay);
}

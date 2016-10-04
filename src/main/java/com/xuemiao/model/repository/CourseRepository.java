package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.CourseEntity;
import com.xuemiao.model.pdm.StudentAndCourseNameKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by dzj on 9/30/2016.
 */
public interface CourseRepository extends JpaRepository<CourseEntity, StudentAndCourseNameKey> {
    @Query("select c from CourseEntity c where c.studentId = :studentId and (:currentWeek between c.startWeek and c.endWeek)")
    List<CourseEntity> getCoursesByStudentAndWeek(@Param("studentId") Long studentId,
                                                  @Param("currentWeek") int currentWeek);
}

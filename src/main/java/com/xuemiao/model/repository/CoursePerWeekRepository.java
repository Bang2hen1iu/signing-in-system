package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.CoursePerWeekEntity;
import com.xuemiao.model.pdm.CoursePerWeekPKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dzj on 10/4/2016.
 */
public interface CoursePerWeekRepository extends JpaRepository<CoursePerWeekEntity, CoursePerWeekPKey> {
    @Query("select c from CoursePerWeekEntity c where c.studentId = :studentId and c.courseName = :courseName and c.weekday = :weekday")
    CoursePerWeekEntity findOneByIdAndNameAndWeekday(@Param("studentId") Long studentId,
                                                     @Param("courseName") String courseName,
                                                     @Param("weekday") int weekday);

    @Query("select c from CoursePerWeekEntity c where c.studentId = :studentId and c.courseName = :courseName")
    List<CoursePerWeekEntity> findByIdAndName(@Param("studentId") Long studentId,
                                              @Param("courseName") String courseName);

    @Transactional
    @Modifying
    @Query("delete from CoursePerWeekEntity c where c.studentId = :studentId and c.courseName = :courseName")
    void deleteByStudentIdAndCourseName(@Param("studentId") Long studentId,
                                        @Param("courseName") String courseName);
}

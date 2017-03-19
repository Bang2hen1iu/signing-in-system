package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.CoursePerWeekEntity;
import com.xuemiao.model.pdm.primaryKey.CoursePerWeekPKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dzj on 10/4/2016.
 */
@Component
public interface CoursePerWeekRepository extends JpaRepository<CoursePerWeekEntity, CoursePerWeekPKey> {
    @Query("select c from CoursePerWeekEntity c where c.courseId = :courseId")
    List<CoursePerWeekEntity> findByCourseId(@Param("courseId") Long courseId);

    @Transactional
    @Modifying
    @Query("delete from CoursePerWeekEntity c where c.courseId = :courseId")
    void deleteByCourseId(@Param("courseId") Long courseId);

}

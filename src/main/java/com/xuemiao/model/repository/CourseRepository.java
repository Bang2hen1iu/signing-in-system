package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.CourseEntity;
import com.xuemiao.model.pdm.SemesterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dzj on 9/30/2016.
 */
@Component
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    @Query("select c from CourseEntity c where c.studentId = :studentId and (:currentWeek between c.startWeek and c.endWeek) and c.semesterId = :semesterId")
    List<CourseEntity> getCoursesByStudentAndWeekAndSemesterId(@Param("studentId") Long studentId,
                                                  @Param("currentWeek") int currentWeek, @Param("semesterId") Long semesterId);

    List<CourseEntity> findByStudentId(Long studentId);

    List<CourseEntity> findByStudentIdAndSemesterId(Long studentId, Long semesterId);

    @Transactional
    @Query("delete from CourseEntity s where s.studentId = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);

    @Query("select from CourseEntity s where s.semesterId = :semesterId")
    List<CourseEntity> findBySemesterId(@Param("semesterId") Long semesterId);
}

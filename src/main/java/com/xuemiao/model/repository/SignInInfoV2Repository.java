package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.SignInInfoV2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

/**
 * Created by root on 16-10-17.
 */
@Component
public interface SignInInfoV2Repository extends JpaRepository<SignInInfoV2Entity, Long> {
    @Query("select s from SignInInfoV2Entity s where s.studentId=:studentId and s.operDate=:date")
    SignInInfoV2Entity findOneByStudentIdAndDate(@Param("studentId") Long studentId, @Param("date") Date date);

    List<SignInInfoV2Entity> findByOperDate(Date date);

    @Query("select s from SignInInfoV2Entity s where s.studentId=:studentId and s.operDate between :startDate and :endDate")
    List<SignInInfoV2Entity> findByStudentIdAndStartDateAndEndDate(@Param("studentId") Long studentId,
                                                                   @Param("startDate") Date startDate,
                                                                   @Param("endDate") Date endDate);

    @Query("select s from SignInInfoV2Entity s where s.studentId=:studentId and s.operDate >= :startDate")
    List<SignInInfoV2Entity> findByStudentIdAndStartDate(@Param("studentId") Long studentId, @Param("startDate") Date startDate);

    @Query("select max(s.operDate) from SignInInfoV2Entity s")
    Date getLatestSignInInfoDate();

    @Transactional
    @Modifying
    @Query("delete from SignInInfoV2Entity s where s.studentId = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);

    List<SignInInfoV2Entity> findByStudentId(Long studentId);
}

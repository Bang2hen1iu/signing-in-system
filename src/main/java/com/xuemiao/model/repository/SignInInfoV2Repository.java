package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.SignInInfoEntity;
import com.xuemiao.model.pdm.SignInInfoV2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

/**
 * Created by root on 16-10-17.
 */
@Component
public interface SignInInfoV2Repository extends JpaRepository<SignInInfoV2Entity,Long>{
    @Query("select s from SignInInfoV2Entity s where s.studentId=:studentId and s.operDate=:date")
    SignInInfoV2Entity findOneByStudentIdAndDate(@Param("studentId") Long studentId, @Param("date") Date date);

    List<SignInInfoV2Entity> findByOperDate(Date date);

    @Query("select max(s.operDate) from SignInInfoV2Entity s")
    Date getLatestSignInInfoDate();
}

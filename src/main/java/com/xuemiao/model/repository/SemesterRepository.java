package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.SemesterEntity;
import com.xuemiao.model.pdm.SignInInfoV2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

/**
 * Created by zijun on 17-3-19.
 */
@Component
public interface SemesterRepository extends JpaRepository<SemesterEntity, Long>{
    @Query("select s from SemesterEntity s order by s.startDate desc")
    List<SemesterEntity> findSemestersOrderByStartDateDesc();
}

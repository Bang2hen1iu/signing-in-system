package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.DutyStudentEntity;
import com.xuemiao.model.pdm.primaryKey.DutyStudentPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

/**
 * Created by dzj on 9/30/2016.
 */
@Component
public interface DutyStudentRepository extends JpaRepository<DutyStudentEntity, DutyStudentPK> {
    @Query("select d from DutyStudentEntity d where :date between d.startDate and d.endDate")
    List<DutyStudentEntity> findByOperDate(@Param("date") Date date);

    @Transactional
    void deleteByStudentId(Long studentId);
}

package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.DutyStudentEntity;
import com.xuemiao.model.pdm.DutyStudentPK;
import com.xuemiao.model.pdm.StudentIdAndOperDateKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

/**
 * Created by dzj on 9/30/2016.
 */
public interface DutyStudentRepository extends JpaRepository<DutyStudentEntity, DutyStudentPK> {
    @Query("select d from DutyStudentEntity d where d.startDate <= :date and d.endDate >= :date")
    List<DutyStudentEntity> findByOperDate(@Param("date") Date date);

    @Transactional
    void deleteByStudentId(Long studentId);
}

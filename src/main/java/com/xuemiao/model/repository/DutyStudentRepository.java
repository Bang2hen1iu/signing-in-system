package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.DutyStudentEntity;
import com.xuemiao.model.pdm.StudentIdAndOperDateKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by dzj on 9/30/2016.
 */
public interface DutyStudentRepository extends JpaRepository<DutyStudentEntity, StudentIdAndOperDateKey> {
    List<DutyStudentEntity> findByOperDate(Date date);
}

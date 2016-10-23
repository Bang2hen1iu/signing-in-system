package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.AbsenceEntity;
import com.xuemiao.model.pdm.primaryKey.StudentIdAndOperDateKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dzj on 9/30/2016.
 */
@Component
public interface AbsenceRepository extends JpaRepository<AbsenceEntity, Long> {

}

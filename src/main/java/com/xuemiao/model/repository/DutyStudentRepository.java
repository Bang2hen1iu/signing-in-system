package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.DutyStudentEntity;
import com.xuemiao.model.pdm.StudentIdAndOperDateKey;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by dzj on 9/30/2016.
 */
public interface DutyStudentRepository extends CrudRepository<DutyStudentEntity, StudentIdAndOperDateKey>{
}

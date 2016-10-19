package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dzj on 9/30/2016.
 */
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
}

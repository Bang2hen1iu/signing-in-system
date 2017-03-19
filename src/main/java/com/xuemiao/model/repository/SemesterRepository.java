package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.SemesterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Created by zijun on 17-3-19.
 */
@Component
public interface SemesterRepository extends JpaRepository<SemesterEntity, Long>{
}

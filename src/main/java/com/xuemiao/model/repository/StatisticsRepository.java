package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.StatisticsEntity;
import com.xuemiao.model.pdm.StudentIdAndOperDateKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dzj on 10/1/2016.
 */
public interface StatisticsRepository extends JpaRepository<StatisticsEntity, StudentIdAndOperDateKey>{
}

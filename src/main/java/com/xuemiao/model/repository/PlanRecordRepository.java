package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.PlanRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by b3-542 on 7/14/17.
 */
@Component
public interface PlanRecordRepository extends JpaRepository<PlanRecordEntity, Long> {
    @Query("select p from PlanRecordEntity p where p.planId=:planId")
    List<PlanRecordEntity> findByPlanId(@Param("planId") Long planId);
}

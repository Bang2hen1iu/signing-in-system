package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.WeekPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by b3-542 on 7/14/17.
 */
@Component
public interface WeekPlanRepository extends JpaRepository<WeekPlanEntity, Long> {
    @Query("select w from WeekPlanEntity w order by w.createAt desc")
    List<WeekPlanEntity> findWeekPlansOrderByCreateAt();

    @Query("select max(w.createAt) from WeekPlanEntity w")
    Timestamp getLatestWeekPlan();
}

package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.StatisticsEntity;
import com.xuemiao.model.pdm.StudentIdAndOperDateKey;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by dzj on 10/1/2016.
 */
public interface StatisticsRepository extends JpaRepository<StatisticsEntity, StudentIdAndOperDateKey>{
    List<StatisticsEntity> findByOperDate(DateTime dateTime);
    @Query("select distinct s.operDate from StatisticsEntity s")
    List<DateTime> getAllStatisticsDate();
    @Query("select s.studentId, sum(s.stayLabTime), sum(s.absenceTimes) from StatisticsEntity s where s.operDate >= :startDate and s.operDate <= :endDate group by s.studentId")
    List<Object[]> getRangeStatistics(@Param("startDate") DateTime startDate, @Param("endDate") DateTime endDate);
}

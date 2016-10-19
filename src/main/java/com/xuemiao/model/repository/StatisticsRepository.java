package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.StatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

/**
 * Created by dzj on 10/1/2016.
 */
@Component
public interface StatisticsRepository extends JpaRepository<StatisticsEntity, Long> {
    List<StatisticsEntity> findByOperDate(Date date);

    @Query("select distinct s.operDate from StatisticsEntity s")
    List<Date> getAllStatisticsDate();

    @Query("select s.studentId, sum(s.stayLabTime), sum(s.absenceTimes) from StatisticsEntity s where s.operDate >= :startDate and s.operDate <= :endDate group by s.studentId")
    List<Object[]> getRangeStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select s.studentId, sum(s.stayLabTime), sum(s.absenceTimes) from StatisticsEntity s group by s.studentId")
    List<Object[]> getStatisticsSum();

    @Query("select s.studentId, sum(s.stayLabTime), sum(s.absenceTimes) from StatisticsEntity s where s.operDate >= :startDate group by s.studentId")
    List<Object[]> getStatisticsByStartDate(@Param("startDate") Date startDate);

    @Query("select max(s.operDate) from StatisticsEntity s")
    Date getLatestStatisticsDate();

    @Transactional
    @Query("delete from StatisticsEntity s where s.studentId = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);


}

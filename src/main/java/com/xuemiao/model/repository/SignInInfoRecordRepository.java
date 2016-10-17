package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.SignInInfoRecordEntity;
import com.xuemiao.model.pdm.SignInInfoRecordPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by root on 16-10-17.
 */
@Component
public interface SignInInfoRecordRepository extends JpaRepository<SignInInfoRecordEntity,SignInInfoRecordPK>{
    @Query("select s from SignInInfoRecordEntity s where s.signInId=:id")
    List<SignInInfoRecordEntity> findBySignInId(@Param("id") Long id);

    @Query("select s from SignInInfoRecordEntity s where s.signInId=:id and s.startTime!=null and s.endTime=null")
    SignInInfoRecordEntity findOneUnfinishedSignInRecord(@Param("id") Long id);
}

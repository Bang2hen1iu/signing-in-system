package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.SignInInfoRecordEntity;
import com.xuemiao.model.pdm.primaryKey.SignInInfoRecordPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by root on 16-10-17.
 */
@Component
public interface SignInInfoRecordRepository extends JpaRepository<SignInInfoRecordEntity,SignInInfoRecordPK>{
    @Query("select s from SignInInfoRecordEntity s where s.signInInfoId=:signInInfoId")
    List<SignInInfoRecordEntity> findBySignInInfoId(@Param("signInInfoId") Long signInInfoId);

    @Query("select s from SignInInfoRecordEntity s where s.signInInfoId=:signInInfoId and s.startTime is not null and s.endTime is null")
    SignInInfoRecordEntity findOneUnfinishedSignInRecord(@Param("signInInfoId") Long signInInfoId);

    @Transactional
    @Query("delete from SignInInfoRecordEntity s where s.signInInfoId = :signInInfoId")
    void deleteBySignInInfoId(Long signInInfoId);
}

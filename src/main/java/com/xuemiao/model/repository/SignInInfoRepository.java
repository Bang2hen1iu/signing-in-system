package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.SignInInfoEntity;
import com.xuemiao.model.pdm.StudentIdAndOperDateKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

/**
 * Created by dzj on 9/30/2016.
 */
@Component
public interface SignInInfoRepository extends JpaRepository<SignInInfoEntity, StudentIdAndOperDateKey> {
    List<SignInInfoEntity> findByOperDate(Date date);

    @Query("select distinct s.operDate from SignInInfoEntity s")
    List<Date> getAllSignInInfoDate();

    @Query("select max(s.operDate) from SignInInfoEntity s")
    Date getLatestSignInInfoDate();

}

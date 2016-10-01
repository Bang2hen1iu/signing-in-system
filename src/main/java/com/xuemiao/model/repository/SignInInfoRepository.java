package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.SignInInfoEntity;
import com.xuemiao.model.pdm.StudentIdAndOperDateKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by dzj on 9/30/2016.
 */
public interface SignInInfoRepository extends JpaRepository<SignInInfoEntity, StudentIdAndOperDateKey> {
    List<SignInInfoEntity> findByOperDate(Date date);
    @Query("select distinct s.operDate from SignInInfoEntity s")
    List<Date> getAllSignInInfoDate();
}

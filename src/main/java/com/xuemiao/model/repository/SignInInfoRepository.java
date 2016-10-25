package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.SignInInfoEntity;
import com.xuemiao.model.pdm.primaryKey.StudentIdAndOperDateKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by root on 16-10-25.
 */
public interface SignInInfoRepository extends JpaRepository<SignInInfoEntity,StudentIdAndOperDateKey>{

}

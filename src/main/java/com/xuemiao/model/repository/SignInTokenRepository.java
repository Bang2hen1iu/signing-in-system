package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.SignInInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dzj on 9/30/2016.
 */
public interface SignInTokenRepository extends JpaRepository<SignInInfoEntity, String> {
}

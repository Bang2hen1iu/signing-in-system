package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.SignInTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Created by dzj on 9/30/2016.
 */
@Component
public interface SignInTokenRepository extends JpaRepository<SignInTokenEntity, String> {
}

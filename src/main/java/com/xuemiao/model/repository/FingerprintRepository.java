package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.FingerprintEntity;
import com.xuemiao.model.pdm.primaryKey.FingerprintPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Created by root on 16-10-19.
 */
@Component
public interface FingerprintRepository extends JpaRepository<FingerprintEntity, FingerprintPK> {
    FingerprintEntity findByToken(String token);
}

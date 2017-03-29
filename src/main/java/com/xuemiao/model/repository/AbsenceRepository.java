package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.AbsenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dzj on 9/30/2016.
 */
@Component
public interface AbsenceRepository extends JpaRepository<AbsenceEntity, Long> {
    int countBySignInInfoId(Long signInInfoId);

    List<AbsenceEntity> findBySignInInfoId(Long signInInfoId);

    @Transactional
    @Modifying
    void deleteBySignInInfoId(Long signInInfoId);
}

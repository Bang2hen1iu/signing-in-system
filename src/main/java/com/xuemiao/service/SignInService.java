package com.xuemiao.service;

import com.xuemiao.model.pdm.FingerprintEntity;
import com.xuemiao.model.repository.FingerprintRepository;
import com.xuemiao.utils.FingerprintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by root on 16-10-19.
 */
@Component
public class SignInService {
    @Autowired
    FingerprintRepository fingerprintRepository;

    public Long checkFingerPrint(String fingerprint){
        List<FingerprintEntity> fingerprintEntities = fingerprintRepository.findAll();
        for(FingerprintEntity fingerprintEntity : fingerprintEntities){
            if(FingerprintUtils.process(fingerprintEntity.getToken(),fingerprint)){
                return fingerprintEntity.getStudentId();
            }
        }
        return null;
    }
}

package com.xuemiao.service;

import com.xuemiao.exception.IdNotExistException;
import com.xuemiao.exception.PasswordErrorException;
import com.xuemiao.model.pdm.SysAdminEntity;
import com.xuemiao.model.repository.SysAdminRepository;
import com.xuemiao.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by dzj on 10/1/2016.
 */
@Component
public class AdminValidationService {
    @Autowired
    SysAdminRepository sysAdminRepository;

    public void testPassword(Long id, String password, int type)
    throws IdNotExistException, PasswordErrorException{
        SysAdminEntity sysAdminEntity = sysAdminRepository.findOne(id);
        if(sysAdminEntity==null||sysAdminEntity.getType()!=type){
            throw new IdNotExistException();
        }
        else if(!PasswordUtils.isPasswordCorrect(password, sysAdminEntity.getPasswordSalted())){
            throw new PasswordErrorException();
        }
    }
}

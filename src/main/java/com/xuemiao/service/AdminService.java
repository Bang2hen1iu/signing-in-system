package com.xuemiao.service;

import com.xuemiao.exception.IdNotExistException;
import com.xuemiao.exception.PasswordErrorException;
import com.xuemiao.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by dzj on 10/1/2016.
 */
@Component
public class AdminService {

    @Value("${admin.id}")
    String adminId;
    @Value("${admin.salted-password}")
    String saltedPassword;

    public void testPassword(String id, String password)
            throws IdNotExistException, PasswordErrorException {
        if (!this.adminId.equals(id)) {
            throw new IdNotExistException();
        } else if (!PasswordUtils.isPasswordCorrect(password, saltedPassword)) {
            throw new PasswordErrorException();
        }
    }

}

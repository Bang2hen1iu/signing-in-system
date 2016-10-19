package com.xuemiao.service;

import com.xuemiao.exception.TokenInvalidException;
import com.xuemiao.model.pdm.SignInTokenEntity;
import com.xuemiao.model.repository.SignInTokenRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Created by dzj on 10/1/2016.
 */
@Component
public class CookieValidationService {
    @Value("${admin.cookie.token.name}")
    String tokenName;
    @Autowired
    SignInTokenRepository signInTokenRepository;

    public void checkTokenCookie(String tokenString, int type) throws TokenInvalidException {
        boolean flag = true;
        if (tokenString == null) {
            flag = false;
        } else {
            SignInTokenEntity signInTokenEntity = signInTokenRepository.findOne(tokenString);
            if (signInTokenEntity == null || signInTokenEntity.getExpireAt().compareTo(new Date()) == -1) {
                flag = false;
            }
        }
        if (!flag) {
            throw new TokenInvalidException();
        }
    }

    public NewCookie getTokenCookie(String path, int age) {
        String token = UUID.randomUUID().toString();
        SignInTokenEntity signInTokenEntity = new SignInTokenEntity();
        DateTime now = DateTime.now();
        DateTime expireDate = now.plusSeconds(age);
        signInTokenEntity.setSignInAt(new Timestamp(now.getMillis()));
        signInTokenEntity.setExpireAt(new Timestamp(expireDate.getMillis()));
        signInTokenEntity.setToken(token);
        signInTokenRepository.save(signInTokenEntity);
        Cookie cookie = new Cookie(tokenName, token, path, null);
        return new NewCookie(cookie, null, age, false);
    }

    public NewCookie refreshCookie(String oriTokenString, String path, int age) {
        SignInTokenEntity signInTokenEntity = signInTokenRepository.findOne(oriTokenString);
        if (signInTokenEntity == null) {
            return null;
        }
        return getTokenCookie(path, age);
    }

    public void deleteCookieByToken(String tokenString) {
        signInTokenRepository.delete(tokenString);
    }
}

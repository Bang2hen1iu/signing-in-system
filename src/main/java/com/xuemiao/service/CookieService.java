package com.xuemiao.service;

import com.xuemiao.exception.TokenInvalidException;
import com.xuemiao.model.pdm.SignInTokenEntity;
import com.xuemiao.model.repository.SignInTokenRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Created by dzj on 10/1/2016.
 */
@Service
public class CookieService {
    @Value("${admin.cookie.token.name}")
    String tokenName;
    @Autowired
    SignInTokenRepository signInTokenRepository;
    @Value("${admin.cookie.token.age}")
    int cookieAge;
    @Value("${admin.cookie.token.path}")
    String cookiePath;

    public void checkTokenCookie(String tokenString) throws TokenInvalidException {
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

    private NewCookie getTokenCookie(String path, int age) {
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

    private NewCookie refreshCookie(String oriTokenString, String path, int age) {
        SignInTokenEntity signInTokenEntity = signInTokenRepository.findOne(oriTokenString);
        if (signInTokenEntity == null) {
            return null;
        }
        return getTokenCookie(path, age);
    }

    public void deleteCookieByToken(String tokenString) {
        signInTokenRepository.delete(tokenString);
    }

    public NewCookie getCookie() {
        return this.getTokenCookie(cookiePath, cookieAge);
    }

    public NewCookie refreshCookie(String tokenString) {
        return this.refreshCookie(tokenString, cookiePath, cookieAge);
    }
}

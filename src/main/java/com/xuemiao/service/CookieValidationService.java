package com.xuemiao.service;

import com.xuemiao.model.pdm.SignInTokenEntity;
import com.xuemiao.model.repository.SignInTokenRepository;
import com.xuemiao.model.repository.SysAdminRepository;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;
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
    @Autowired
    SysAdminRepository sysAdminRepository;

    //返回-1表示token不存在，
    public Response checkTokenCookie(String tokenString, int type){
        boolean flag = true;
        if(tokenString==null){
            flag = false;
        }
        else {
            SignInTokenEntity signInTokenEntity = signInTokenRepository.findOne(tokenString);
            if(signInTokenEntity==null||signInTokenEntity.getExpireAt().compareTo(new Date())==-1||
                    type!=sysAdminRepository.findOne(signInTokenEntity.getAdminId()).getType()){
                flag = false;
            }
        }
        if(!flag){
            if(type==1){
                return Response.seeOther(URI.create("/index/login")).build();
            }
            else if(type==2){
                return Response.seeOther(URI.create("/admin/login")).build();
            }
        }
        return null;
    }

    public NewCookie getTokenCookie(Long id, String path, int age){
        String token = UUID.randomUUID().toString();
        SignInTokenEntity signInTokenEntity = new SignInTokenEntity();
        signInTokenEntity.setAdminId(id);
        Date nowDate = new Date();
        Date expireDate = DateUtils.addSeconds(nowDate, age);
        signInTokenEntity.setSignInAt(new Timestamp(nowDate.getTime()));
        signInTokenEntity.setExpireAt(new Timestamp(expireDate.getTime()));
        signInTokenEntity.setToken(token);
        signInTokenRepository.save(signInTokenEntity);
        Cookie cookie = new Cookie(tokenName, token, path, null);
        return new NewCookie(cookie, null, age, false);
    }

    public NewCookie refreshCookie(String oriTokenString, String path, int age){
        SignInTokenEntity signInTokenEntity = signInTokenRepository.findOne(oriTokenString);
        if(signInTokenEntity==null){
            return null;
        }
        return getTokenCookie(signInTokenEntity.getAdminId(), path, age);
    }
}

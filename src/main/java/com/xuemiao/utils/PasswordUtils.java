package com.xuemiao.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.Random;

/**
 * Created by dzj on 10/1/2016.
 */
public class PasswordUtils {
    public static final int hashIteration = 1024;

    public static boolean isPasswordCorrect(String password, String passwordHash) {
        // hash:salt:iterations
        String[] part = StringUtils.split(passwordHash, ":");
        String hashPart = part[0];
        String saltPart = part[1];
        int iterationPart = Integer.parseInt(part[2]);
        String hashValue = new Sha256Hash(password, saltPart, iterationPart).toHex();
        return hashPart.equals(hashValue);
    }

    public static String createPasswordHash(String password) {
        byte[] saltByte = new byte[32];
        new Random().nextBytes(saltByte);
        String salt = Base64.encodeBase64String(saltByte);
        Sha256Hash sha256Hash = new Sha256Hash(password, salt, hashIteration);
        String passwordHash = sha256Hash.toHex() + ":" + salt + ":" + hashIteration;
        return passwordHash;
    }
}

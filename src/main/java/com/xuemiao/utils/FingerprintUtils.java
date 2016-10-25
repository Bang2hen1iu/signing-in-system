package com.xuemiao.utils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by root on 16-10-19.
 */
public class FingerprintUtils {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String url = "http://123.207.25.133:8080/FPCom/process";

    public static boolean process(String salted_fingerprint, String raw_fingerprint) {
        OkHttpClient client = new OkHttpClient();
        String params = "?regStr=" + salted_fingerprint + "&logStr=" + raw_fingerprint;
        Request request = new Request.Builder().url(url + params).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string().equals("true");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

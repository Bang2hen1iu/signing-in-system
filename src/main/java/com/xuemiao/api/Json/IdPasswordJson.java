package com.xuemiao.api.Json;

/**
 * Created by dzj on 10/2/2016.
 */
public class IdPasswordJson {
    private Long id;
    private String password1;
    private String password2;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}

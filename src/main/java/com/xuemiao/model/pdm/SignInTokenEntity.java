package com.xuemiao.model.pdm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by dzj on 9/30/2016.
 */
@Entity
@Table(name = "sign_in_token")
public class SignInTokenEntity {
    @Id
    @Column(name = "token")
    private String token;
    @Column(name = "admin_id")
    private Long adminId;
    @Column(name = "sign_in_at")
    private Timestamp signInAt;
    @Column(name = "expire_at")
    private Timestamp expireAt;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Timestamp getSignInAt() {
        return signInAt;
    }

    public void setSignInAt(Timestamp signInAt) {
        this.signInAt = signInAt;
    }

    public Timestamp getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Timestamp expireAt) {
        this.expireAt = expireAt;
    }
}

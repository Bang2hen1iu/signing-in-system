package com.xuemiao.model.pdm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by dzj on 9/30/2016.
 */
@Entity
@Table(name = "sys_admin")
public class SysAdminEntity {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "password_salted")
    private String passwordSalted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPasswordSalted() {
        return passwordSalted;
    }

    public void setPasswordSalted(String passwordSalted) {
        this.passwordSalted = passwordSalted;
    }
}

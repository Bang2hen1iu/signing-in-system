package com.xuemiao.model.pdm;

import javax.persistence.*;

/**
 * Created by dzj on 9/30/2016.
 */
@Entity
@Table(name = "sys_admin")
public class SysAdminEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;
    @Column(name = "type")
    private int type;
    @Column(name = "password_salted")
    private String passwordSalted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPasswordSalted() {
        return passwordSalted;
    }

    public void setPasswordSalted(String passwordSalted) {
        this.passwordSalted = passwordSalted;
    }
}

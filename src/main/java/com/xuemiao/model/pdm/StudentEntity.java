package com.xuemiao.model.pdm;

import javax.persistence.*;

/**
 * Created by dzj on 9/30/2016.
 */
@Entity
@Table(name = "student")
public class StudentEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long studentId;
    @Column(name = "name")
    private String name;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

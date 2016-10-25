package com.xuemiao.model.pdm;

import com.xuemiao.model.pdm.primaryKey.StudentIdAndOperDateKey;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by root on 16-10-25.
 */
@Entity
@Table(name = "sign_in_info")
@IdClass(StudentIdAndOperDateKey.class)
public class SignInInfoEntity implements Serializable{
    @Id
    private Long studentId;
    @Id
    private Date operDate;


}

package com.xuemiao.model.pdm;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by zijun on 17-3-19.
 */
@Entity
@Table(name = "semester")
public class SemesterEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;
    @Column(name = "start_date")
    private Date startDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}

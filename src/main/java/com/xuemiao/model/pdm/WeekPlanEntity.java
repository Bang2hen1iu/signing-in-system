package com.xuemiao.model.pdm;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by b3-542 on 7/14/17.
 */
@Entity
@Table(name = "week_plan")
public class WeekPlanEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;
    @Column(name = "week_name")
    private String weekName;
    @Column(name = "create_at")
    private Timestamp createAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}

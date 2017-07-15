package com.xuemiao.model.pdm;

import javax.persistence.*;
import javax.ws.rs.DefaultValue;

/**
 * Created by b3-542 on 7/14/17.
 */
@Entity
@Table(name = "plan_record")
public class PlanRecordEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;
    @Column(name = "plan_id")
    private Long planId;
    @Column(name = "student_id")
    private Long studentId;
    @Column(name = "plan")
    private String plan;
    @Column(name = "achievement")
    @DefaultValue("")
    private String achievement;
    @Column(name = "tutor_feedback")
    @DefaultValue("")
    private String tutorFeedback;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public String getTutorFeedback() {
        return tutorFeedback;
    }

    public void setTutorFeedback(String tutorFeedback) {
        this.tutorFeedback = tutorFeedback;
    }
}

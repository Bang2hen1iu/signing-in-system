package com.xuemiao.api.Json;

/**
 * Created by b3-542 on 7/15/17.
 */
public class PlanRecordJson {
    private Long id;
    private String studentName;
    private String plan;
    private String achievement;
    private String tutorFeedback;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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

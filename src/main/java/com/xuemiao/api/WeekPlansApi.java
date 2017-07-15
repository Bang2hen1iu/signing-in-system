package com.xuemiao.api;

import com.xuemiao.api.Json.PlanRecordJson;
import com.xuemiao.exception.ParamIncompleteException;
import com.xuemiao.model.pdm.PlanRecordEntity;
import com.xuemiao.model.pdm.WeekPlanEntity;
import com.xuemiao.model.repository.PlanRecordRepository;
import com.xuemiao.model.repository.StudentRepository;
import com.xuemiao.model.repository.WeekPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b3-542 on 7/14/17.
 */
@Path("/week_plans")
public class WeekPlansApi {
    @Autowired
    WeekPlanRepository weekPlanRepository;
    @Autowired
    PlanRecordRepository planRecordRepository;
    @Autowired
    StudentRepository studentRepository;

    @GET
    public Response getWeekPlanList(){
        List<WeekPlanEntity> weekPlanEntities = weekPlanRepository.findWeekPlansOrderByCreateAt();
        return Response.ok().entity(weekPlanEntities).build();
    }

    @GET
    @Path("/{plan_id}")
    public Response getWeekPlanById(@PathParam("plan_id") Long plan_id){
        List<PlanRecordEntity> planRecordEntities = planRecordRepository.findByPlanId(plan_id);
        List<PlanRecordJson> planRecordJsons = new ArrayList<>();
        int length = planRecordEntities.size();
        for (int i = length - 1; i >= 0; i--){
            PlanRecordEntity planRecordEntity = planRecordEntities.get(i);
            PlanRecordJson planRecordJson = new PlanRecordJson();
            planRecordJson.setId(planRecordEntity.getId());
            String studentName = studentRepository.findOne(planRecordEntity.getStudentId()).getName();
            planRecordJson.setStudentName(studentName);
            planRecordJson.setPlan(planRecordEntity.getPlan());
            planRecordJson.setAchievement(planRecordEntity.getAchievement());
            planRecordJson.setTutorFeedback(planRecordEntity.getTutorFeedback());
            planRecordJsons.add(planRecordJson);
        }
        return Response.ok().entity(planRecordJsons).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOrModifyStudentWeekPlan(PlanRecordEntity planRecordEntity) throws ParamIncompleteException {
        Long id = planRecordEntity.getId();
        if (id != null){
            PlanRecordEntity toWritePlanRecordEntity = planRecordRepository.findOne(id);
            toWritePlanRecordEntity.setPlan(planRecordEntity.getPlan());
            planRecordRepository.save(toWritePlanRecordEntity);
            return Response.ok().build();
        }
        else {
            throw new ParamIncompleteException();
        }
    }

    @POST
    @Path("/achievements")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOrModifyStudentWeekAchievement(PlanRecordEntity planRecordEntity) throws ParamIncompleteException {
        Long id = planRecordEntity.getId();
        if (id != null) {
            PlanRecordEntity toWritePlanRecordEntity = planRecordRepository.findOne(id);
            toWritePlanRecordEntity.setAchievement(planRecordEntity.getAchievement());
            planRecordRepository.save(toWritePlanRecordEntity);
            return Response.ok().build();
        }
        else {
            throw new ParamIncompleteException();
        }
    }

    @POST
    @Path("/tutor_feedback")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOrModifyTutorWeekFeedback(PlanRecordEntity planRecordEntity) throws ParamIncompleteException {
        Long id = planRecordEntity.getId();
        if (id != null) {
            PlanRecordEntity toWritePlanRecordEntity = planRecordRepository.findOne(id);
            toWritePlanRecordEntity.setTutorFeedback(planRecordEntity.getTutorFeedback());
            planRecordRepository.save(toWritePlanRecordEntity);
            return Response.ok().build();
        }
        else {
            throw new ParamIncompleteException();
        }
    }

}

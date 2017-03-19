package com.xuemiao.service;

import com.xuemiao.model.pdm.SemesterEntity;
import com.xuemiao.model.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by zijun on 17-3-19.
 */
@Service
public class SemesterService {
    @Autowired
    SemesterRepository semesterRepository;

    public SemesterEntity getLatestSemester(){
        List<SemesterEntity> semesterEntities = semesterRepository.findSemestersOrderByStartDateDesc();
        return semesterEntities.get(0);
    }

    public List<SemesterEntity> getSemesterDescByStartDate(){
        return semesterRepository.findSemestersOrderByStartDateDesc();
    }

    public Long checkSemesterByDate(Date date){
        List<SemesterEntity> semesterEntities = getSemesterDescByStartDate();
        Long semesterId = null;
        for (SemesterEntity semesterEntity : semesterEntities){
            if (date.compareTo(semesterEntity.getStartDate()) >= 0){
                semesterId = semesterEntity.getId();
                break;
            }
        }
        return semesterId;
    }
}

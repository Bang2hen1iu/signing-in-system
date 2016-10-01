package com.xuemiao;

import com.xuemiao.service.ScheduleTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Component;

/**
 * Created by dzj on 10/1/2016.
 */
@Component
public class StartUpInitializer implements ApplicationListener<ApplicationReadyEvent>{
    @Autowired
    ScheduleTaskService scheduleTaskService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        scheduleTaskService.startRefreshSignInfoTable();
    }
}

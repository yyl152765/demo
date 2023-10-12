package com.example.shopdms.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

@Configuration
public class ScheduleConfig implements SchedulingConfigurer {
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    static Logger logger = LoggerFactory.getLogger(ScheduleConfig.class);
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        logger.info("动态定时任务开启");
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(8,threadPoolTaskExecutor));
    }
}

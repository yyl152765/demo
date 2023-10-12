package com.sz.jh.rpa.config;

import com.sz.jh.rpa.task.RemoveMessageTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

@Configuration
public class SchedulerConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @Bean
    public RemoveMessageTask cleanupTask() {
        return new RemoveMessageTask();
    }
}
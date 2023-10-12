package com.sz.jh.rpa.service.impl;

import com.sz.jh.rpa.task.RemoveMessageTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;
@Service
public class TaskControlService {

    @Autowired
    private TaskScheduler taskScheduler;

    private ScheduledFuture<?> scheduledTask;
    private final RemoveMessageTask removeMessageTask;

    public TaskControlService(RemoveMessageTask cleanupTask) {
        this.removeMessageTask = cleanupTask;
    }

    public void startTask() {
        if (scheduledTask == null || scheduledTask.isDone()) {
            scheduledTask = taskScheduler.scheduleAtFixedRate(removeMessageTask, Duration.ofSeconds(5));
            System.out.println("定时任务开始...");
        } else {
            System.out.println("定时任务已在运行中...");
        }
    }

    public void stopTask() {
        if (scheduledTask != null && !scheduledTask.isDone()) {
            scheduledTask.cancel(true);
            System.out.println("定时任务停止...");
        } else {
            System.out.println("定时任务已停止或尚未开始...");
        }
    }
}

package com.sz.jh.rpa.task;

import com.sz.jh.rpa.dao.MessageInfoMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RemoveMessageTask implements Runnable{
    @Autowired
    private MessageInfoMapper messageInfoMapper;
    @Override
    public void run() {
        System.out.println("正在删除所有数据！");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinuteBefore = now.minusMinutes(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = oneMinuteBefore.format(formatter);
        boolean b =messageInfoMapper.DeleteMessageTask(formattedDate);
        System.out.println(b);
    }
}

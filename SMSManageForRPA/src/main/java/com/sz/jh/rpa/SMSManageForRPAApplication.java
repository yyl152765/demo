package com.sz.jh.rpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SMSManageForRPAApplication {

    public static void main(String[] args) {
        SpringApplication.run(SMSManageForRPAApplication.class, args);
    }

}

package com.caili.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableScheduling                // 开启定时任务
@EnableTransactionManagement//开启事务
@EnableAsync//开启Springboot对于异步任务的支持
public class AdviceStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdviceStoreApplication.class, args);
    }


}

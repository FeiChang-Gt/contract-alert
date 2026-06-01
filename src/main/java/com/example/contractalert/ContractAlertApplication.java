package com.example.contractalert;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
        ValidationAutoConfiguration.class,   // 解决 methodValidationPostProcessor 冲突
        SqlInitializationAutoConfiguration.class  // 解决数据库初始化冲突
})
@MapperScan("com.example.contractalert.biz.Mapper")
@EnableScheduling  // 👈 只加这一行！开启定时任务
public class ContractAlertApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContractAlertApplication.class, args);
    }
}
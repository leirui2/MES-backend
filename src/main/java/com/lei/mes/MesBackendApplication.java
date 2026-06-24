package com.lei.mes;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author lei
 */
@SpringBootApplication
@MapperScan("com.lei.mes.mapper")
@EnableScheduling  // 开启定时任务
@EnableTransactionManagement // 开启事务
@EnableAspectJAutoProxy // 开启AOP
public class MesBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MesBackendApplication.class, args);
    }

}

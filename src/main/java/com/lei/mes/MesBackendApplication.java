package com.lei.mes;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lei
 */
@SpringBootApplication
@MapperScan("com.lei.mes.mapper")
public class MesBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MesBackendApplication.class, args);
    }

}

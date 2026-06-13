package com.lei.mes.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lei
 */
@RestController
@RequestMapping("/test")
public class test {

    // 测试接口
    //http://localhost:8083/test/hello
    @RequestMapping("/hello")
    public String hello() {
        return "hello worl1d";
    }
}

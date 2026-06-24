package com.lei.mes.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 异步任务配置
 * @author lei
 */
@Configuration
@Slf4j
public class AsyncConfig {

    @Bean(name = "operationLogExecutor")
    public Executor operationLogExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("operation-log-");
        executor.setRejectedExecutionHandler((r, e) ->
                log.warn("操作日志线程池已满，丢弃任务")
        );
        executor.initialize();
        return executor;
    }
}

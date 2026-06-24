package com.lei.mes.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lei.mes.annotation.OperationLog;
import com.lei.mes.entity.SysOperationLog;
import com.lei.mes.service.SysOperationLogService;
import com.lei.mes.util.IPUtil;
import com.lei.mes.util.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * 操作日志切面
 * @author lei
 */
@Aspect
@Component
@Slf4j
public class OperationLogAspect {

    @Autowired
    private SysOperationLogService operationLogService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private Executor operationLogExecutor;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        log.info(">>> 操作日志切面触发: {}", joinPoint.getSignature().toShortString());  // ← 加这行

        long startTime = System.currentTimeMillis();

        // 执行目标方法
        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - startTime;

        // 异步写入日志
        operationLogExecutor.execute(() -> {
            try {
                // 判空：未登录时不记录
                var ctx = UserContextHolder.getUserContext();
                if (ctx == null) {
                    //todo 记录日志没有作用
                    System.out.println("asdasda");
                    log.warn("未登录，不记录操作日志");
                    return;
                }

                SysOperationLog log = new SysOperationLog();
                log.setUserId(ctx.getUserId());
                log.setUsername(ctx.getUserName());
                log.setOperation(operationLog.operation());
                log.setMethod(joinPoint.getSignature().toShortString());

                // 参数保存
                if (operationLog.saveParams()) {
                    log.setParams(formatParams(joinPoint.getArgs()));
                }

                // 结果保存
                if (operationLog.saveResult()) {
                    log.setResult(objectMapper.writeValueAsString(result));
                }

                log.setIp(IPUtil.getRealIP(request));
                log.setDuration((int) duration);
                log.setStatus(1);
                log.setErrorMsg("");

                operationLogService.save(log);
            } catch (Exception e) {
                log.error("记录操作日志失败", e);
            }
        });

        return result;
    }

    /**
     * 格式化请求参数，过滤敏感字段
     */
    private String formatParams(Object[] args) {
        if (args == null || args.length == 0) {
            return "{}";
        }

        try {
            String json = objectMapper.writeValueAsString(args);
            // 过滤敏感信息
            json = json.replaceAll("\"password\"\\s*:\\s*\"[^\"]*\"", "\"password\":\"***\"");
            return json;
        } catch (Exception e) {
            return "[序列化失败]";
        }
    }
}

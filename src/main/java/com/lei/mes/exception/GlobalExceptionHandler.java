package com.lei.mes.exception;

import com.lei.mes.common.ErrorCode;
import com.lei.mes.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * @author lei
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * valid 参数校验异常
     * @author lei
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String msg = error.getDefaultMessage();
            errors.put(field, msg);
        });
        log.warn("参数校验异常: {}", errors);
        return ResponseEntity.status(400).body(Result.error(400, "参数校验失败", errors));
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result> handleBusinessException(BusinessException e) {
        log.warn("业务异常: code={}, msg={}", e.getCode(), e.getMessage());
        return ResponseEntity.status(e.getCode()).body(Result.error(e.getCode(), e.getMessage()));
    }

    /**
     * 404 - 请求方法不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Result> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.warn("请求方法不支持: {}", e.getMessage());
        return ResponseEntity.status(405).body(Result.error(ErrorCode.METHOD_NOT_ALLOWED));
    }

    /**
     * 400 - 请求体为空
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result> handleNotReadable(HttpMessageNotReadableException e) {
        log.warn("请求体为空: {}", e.getMessage());
        return ResponseEntity.badRequest().body(Result.error(400, "请求体不能为空"));
    }

    /**
     * 数据库唯一约束冲突（主键、唯一索引重复）
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Result> handleDuplicateKeyException(DuplicateKeyException e) {
        log.warn("数据重复: {}", e.getMessage());
        String message = "数据已存在，请勿重复操作";
        // 可选：从原始异常中解析冲突值，给前端更具体的提示
        if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
            String causeMsg = e.getCause().getMessage();
            if (causeMsg != null && causeMsg.contains("Duplicate entry")) {
                message = "记录已存在，违反唯一约束";
            }
        }
        return ResponseEntity.status(400).body(Result.error(400, message));
    }
    /**
     * 兜底 - 捕获所有未处理的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleException(Exception e) {
        log.error("系统异常: ", e);
        return ResponseEntity.status(500).body(Result.error(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}

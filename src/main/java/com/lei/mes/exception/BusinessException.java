package com.lei.mes.exception;

import com.lei.mes.common.ErrorCode;
import lombok.Getter;

/**
 * 业务异常类
 * @author lei
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 业务错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
    }

    public BusinessException(String message) {
        super(message);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR.getCode();
    }
}

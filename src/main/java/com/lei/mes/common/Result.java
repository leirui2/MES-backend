package com.lei.mes.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果封装
 * @author lei
 */
@Data
public class Result<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    public Result() {}

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    /**
     * 成功（有数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /**
     * 成功（自定义消息）
     */
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(200, msg, data);
    }

    /**
     * 失败
     */
    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    /**
     * 失败（使用默认消息）
     */
    public static <T> Result<T> error(int code) {
        String msg = ErrorCode.getMessageByCode(code);
        return new Result<>(code, msg == null ? "操作失败" : msg, null);
    }

    /**
     * 失败（使用枚举定义的消息）
     * @param errorCode 错误码枚举
     */
    public static <T> Result<T> error(ErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMsg(), null);
    }

    /**
     * 失败（带自定义数据，如参数校验错误集合）
     */
    public static <T> Result<T> error(int code, String msg, T data) {
        return new Result<>(code, msg, data);
    }
}

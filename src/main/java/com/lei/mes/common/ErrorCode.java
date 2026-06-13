package com.lei.mes.common;

import lombok.Getter;

/**
 * 错误码枚举
 * @author lei
 */
@Getter
public enum ErrorCode {

    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或Token过期"),
    FORBIDDEN(403, "没有权限"),
    NOT_FOUND(404, "请求资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),
    CONFLICT(409, "资源冲突"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂时不可用");

    private final int code;
    private final String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 根据错误码获取消息
     * @param code 错误码
     * @return 错误消息
     */
    public static String getMessageByCode(int code) {
        for (ErrorCode value : values()) {
            if (value.code == code) {
                return value.msg;
            }
        }
        return null;
    }
}

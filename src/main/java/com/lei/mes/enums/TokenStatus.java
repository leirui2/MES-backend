package com.lei.mes.enums;

/**
 * Token 状态枚举
 * @author lei
 */

public enum TokenStatus {
    VALID,      // 有效
    EXPIRED,    // 已过期
    INVALID     // 无效（签名错误、格式错误等）
}
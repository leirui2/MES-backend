package com.lei.mes.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * @author lei
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 操作类型：ADD / EDIT / DELETE / QUERY
     */
    String operation();

    /**
     * 操作描述（可选，显示在界面上）
     */
    String value() default "";

    /**
     * 是否保存请求参数（默认保存）
     */
    boolean saveParams() default true;

    /**
     * 是否保存返回结果（默认不保存）
     */
    boolean saveResult() default false;
}

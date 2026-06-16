package com.lei.mes.util;

/**
 * UserContext 上下文持有类，用于存储和获取当前登录用户信息的 ThreadLocal 变量
 * @author lei
 */
public class UserContextHolder {
    // 定义 ThreadLocal 用于存储用户信息
    private static final ThreadLocal<UserContext> userContextThreadLocal = new ThreadLocal<>();

    public static void setUserContext ( UserContext userContext){
        userContextThreadLocal.set(userContext);
    }

    public static UserContext getUserContext (){
        return userContextThreadLocal.get();
    }

    //清除ThreadLocal
    public static void remove (){
        userContextThreadLocal.remove();
    }
}

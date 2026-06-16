package com.lei.mes.util;

/**
 * 当前用户上下文
 * @author lei
 */
public class UserContext {

    private Long userId;
    private String userName;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

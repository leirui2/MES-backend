package com.lei.mes.vo;

import lombok.Data;

/**
 * @author lei
 */
@Data
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String username;
    private String realName;
    private String email;
    private String phone;
}

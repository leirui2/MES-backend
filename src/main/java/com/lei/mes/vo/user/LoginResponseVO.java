package com.lei.mes.vo.user;

import lombok.Data;

import java.util.List;

/**
 * 登录响应VO
 * @author lei
 */
@Data
public class LoginResponseVO {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String username;
    private String realName;
    //角色列表
    private List<RoleVO> roles;
    private String email;
    private String phone;
}

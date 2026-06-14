package com.lei.mes.vo.user;

import lombok.Data;

import java.util.Date;

/**
 * 用户详情 VO（返回给前端）
 * 不包含密码等敏感字段
 * @author lei
 */
@Data
public class UserVO {

    private Long id;
    private String username;
    private String realName;
    private String phone;
    private String email;
    private String avatar;
    private Integer status;
    private Long deptId;
    private Date lastLoginTime;
    private String lastLoginIp;
    private Date createdAt;
    private Date updatedAt;
}

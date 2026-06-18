package com.lei.mes.vo.user;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户详情 VO
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

    //角色列表
    private List<RoleVO> roles;

    private Long deptId;
    private Date lastLoginTime;
    private String lastLoginIp;
    private Date createdAt;
    private Date updatedAt;
}

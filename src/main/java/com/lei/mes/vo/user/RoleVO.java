package com.lei.mes.vo.user;

import lombok.Data;

import java.util.List;

/**
 * 角色VO
 * @author lei
 */
@Data
public class RoleVO {
    private Long roleId;
    private String roleName;
    private String roleCode;

    // 新增：菜单列表
    private List<MenuVO> menus;
}
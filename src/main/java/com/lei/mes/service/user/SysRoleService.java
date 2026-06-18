package com.lei.mes.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lei.mes.entity.user.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mes.request.user.RoleSaveRequest;
import com.lei.mes.vo.user.RoleVO;
import jakarta.validation.Valid;

import java.util.List;

/**
* @author lei
* @description 针对表【sys_role(角色表)】的数据库操作Service
* @createDate 2026-06-16 16:23:20
*/
public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色列表
     */
    IPage<SysRole> getRolePage(int pageNum, int pageSize, String roleName);

    /**
     * 新增角色
     */
    void addRole(@Valid RoleSaveRequest request);

    /**
     * 更新角色
     */
    void updateRole(@Valid RoleSaveRequest request);

    /**
     * 删除角色
     */
    void deleteRole(Long id);

    /**
     * 更新角色状态
     */
    void changeStatus(Long id, Integer status);

    /**
     * 获取角色详情（含菜单列表）
     */
    RoleVO getRoleVoById(Long roleId);

    /**
     * 更新角色的菜单关联
     */
    void updateRoleMenus(Long roleId, List<Long> menuIds);
}

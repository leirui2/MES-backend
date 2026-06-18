package com.lei.mes.mapper.user;

import com.lei.mes.entity.user.SysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author lei
* @description 针对表【sys_role_menu(角色-菜单关联表)】的数据库操作Mapper
* @createDate 2026-06-17 20:09:00
* @Entity com.lei.mes.entity.user.SysRoleMenu
*/
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * 批量插入角色-菜单关联
     */
    void insertBatch(@Param("list") List<SysRoleMenu> list);

    /**
     * 查询角色的菜单 ID 列表
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
}

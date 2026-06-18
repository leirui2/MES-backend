package com.lei.mes.mapper.user;

import com.lei.mes.entity.user.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lei.mes.vo.user.RoleVO;
import com.lei.mes.vo.user.UserVO;
import org.apache.ibatis.annotations.Param;

/**
* @author lei
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2026-06-16 16:23:20
* @Entity com.lei.mes.entity.user.SysRole
*/
public interface SysRoleMapper extends BaseMapper<SysRole> {


    /**
     * 查询角色及菜单（新增）
     */
    RoleVO getRoleVoById(@Param("roleId") Long roleId);
}





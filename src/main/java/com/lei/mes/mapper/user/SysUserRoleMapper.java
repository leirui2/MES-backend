package com.lei.mes.mapper.user;

import com.lei.mes.entity.user.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
* @author lei
* @description 针对表【sys_user_role(用户-角色关联表)】的数据库操作Mapper
* @createDate 2026-06-17 18:16:22
* @Entity com.lei.mes.entity.user.SysUserRole
*/
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    void insertBatch(@Param("collect") List<SysUserRole> collect);
}





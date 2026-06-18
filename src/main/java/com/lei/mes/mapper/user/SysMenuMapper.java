package com.lei.mes.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lei.mes.entity.user.SysMenu;
import com.lei.mes.vo.user.MenuTreeVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author lei
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 查询所有菜单（按排序）
     */
    List<SysMenu> selectAllMenus();

    /**
     * 查询角色的菜单 ID 列表（用于回显）
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
}

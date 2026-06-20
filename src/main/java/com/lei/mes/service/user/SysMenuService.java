package com.lei.mes.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mes.entity.user.SysMenu;
import com.lei.mes.vo.user.MenuTreeVO;
import java.util.List;

/**
 * @author lei
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 查询菜单树（扁平化转树形）
     */
    List<MenuTreeVO> getMenuTree();

    /**
     * 查询角色可分配的菜单树（带选中状态）
     */
    List<MenuTreeVO> getAssignMenuTree(Long roleId);

}

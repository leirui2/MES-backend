package com.lei.mes.controller.user;

import com.lei.mes.common.Result;
import com.lei.mes.entity.user.SysRole;
import com.lei.mes.service.user.SysMenuService;
import com.lei.mes.service.user.SysRoleService;
import com.lei.mes.vo.user.MenuTreeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单控制器
 * @author lei
 */
@Slf4j
@RestController
@RequestMapping("/api/sys/menu")
public class MenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleService sysRoleService;
    /**
     * 查询菜单树（用于菜单管理页面）
     */
    @GetMapping("/tree")
    public Result<List<MenuTreeVO>> getMenuTree() {
        List<MenuTreeVO> tree = sysMenuService.getMenuTree();
        return Result.success(tree);
    }

    /**
     * 查询角色可分配的菜单树（带选中状态）
     */
    @GetMapping("/assign/{roleId}")
    public Result<List<MenuTreeVO>> getAssignMenuTree(@PathVariable Long roleId) {
        SysRole role = sysRoleService.getById(roleId);
        if (role == null) {
            return Result.error(404, "角色不存在");
        }
        List<MenuTreeVO> tree = sysMenuService.getAssignMenuTree(roleId);
        return Result.success(tree);
    }
}

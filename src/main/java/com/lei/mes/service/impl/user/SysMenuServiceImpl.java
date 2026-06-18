package com.lei.mes.service.impl.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mes.entity.user.SysMenu;
import com.lei.mes.mapper.user.SysMenuMapper;
import com.lei.mes.mapper.user.SysRoleMenuMapper;
import com.lei.mes.service.user.SysMenuService;
import com.lei.mes.vo.user.MenuTreeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 * @author lei
 */
@Slf4j
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
        implements SysMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<MenuTreeVO> getMenuTree() {
        // 1. 查询所有菜单
        List<SysMenu> allMenus = baseMapper.selectAllMenus();

        // 2. 转为 VO
        List<MenuTreeVO> menuVOs = allMenus.stream()
                .map(menu -> {
                    MenuTreeVO vo = new MenuTreeVO();
                    BeanUtils.copyProperties(menu, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        // 3. 构建树形结构
        return buildTree(menuVOs, 0L);
    }

    @Override
    public List<MenuTreeVO> getAssignMenuTree(Long roleId) {
        // 1. 查询所有菜单
        List<SysMenu> allMenus = baseMapper.selectAllMenus();

        // 2. 查询角色已分配的菜单 ID
        List<Long> assignedMenuIds = sysRoleMenuMapper.selectMenuIdsByRoleId(roleId);
        Set<Long> assignedSet = new HashSet<>(assignedMenuIds);

        // 3. 转为 VO 并设置 checked 状态
        List<MenuTreeVO> menuVOs = allMenus.stream()
                .map(menu -> {
                    MenuTreeVO vo = new MenuTreeVO();
                    BeanUtils.copyProperties(menu, vo);
                    vo.setChecked(assignedSet.contains(menu.getId()));
                    return vo;
                })
                .collect(Collectors.toList());

        // 4. 构建树形结构
        return buildTree(menuVOs, 0L);
    }

    /**
     * 递归构建树形结构
     */
    private List<MenuTreeVO> buildTree(List<MenuTreeVO> menus, Long parentId) {
        return menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .peek(menu -> menu.setChildren(
                        buildTree(menus, menu.getId())
                ))
                .sorted(Comparator.comparing(MenuTreeVO::getSortOrder))
                .collect(Collectors.toList());
    }
}

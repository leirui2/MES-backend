package com.lei.mes.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lei.mes.common.Result;
import com.lei.mes.entity.user.SysRole;
import com.lei.mes.entity.user.SysUser;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.request.user.RoleSaveRequest;
import com.lei.mes.request.user.UserSaveRequest;
import com.lei.mes.service.user.SysRoleService;
import com.lei.mes.service.user.SysUserService;
import com.lei.mes.util.UserContext;
import com.lei.mes.util.UserContextHolder;
import com.lei.mes.vo.user.UserVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 角色管理 Controller
 * @author lei
 */
@Slf4j
@RestController
@RequestMapping("/api/sys/role")
public class RoleController {

    @Autowired
    private SysRoleService sysRoleService;


    /**
     * 分页查询角色列表
     */
    @GetMapping("/list")
    public Result<IPage<SysRole>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String roleName) {

        IPage<SysRole> page = sysRoleService.getRolePage(pageNum, pageSize, roleName);
        IPage<SysRole> voPage = page.convert(role -> {
            SysRole vo = new SysRole();
            BeanUtils.copyProperties(role, vo);
            return vo;
        });
        return Result.success(voPage);
    }

    /**
     * 根据 ID 查询角色详情
     */
    @GetMapping("/{id}")
    public Result<SysRole> getById(@PathVariable Long id) {
        SysRole role = sysRoleService.getById(id);
        if (role == null) {
            return Result.error(404, "角色不存在或已删除");
        }
        return Result.success(role);
    }

    /**
     * 新增角色
     */
    @PostMapping
    public Result<Void> add(@Valid @RequestBody RoleSaveRequest request) {
        try {
            sysRoleService.addRole(request);
            return Result.success("新增成功", null);
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 编辑角色
     */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody RoleSaveRequest request) {
        try {
            sysRoleService.updateRole(request);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            sysRoleService.deleteRole(id);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 切换角色状态（启用/禁用）
     */
    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            sysRoleService.changeStatus(id, status);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }
}

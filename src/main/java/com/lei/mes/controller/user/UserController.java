package com.lei.mes.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lei.mes.common.Result;
import com.lei.mes.entity.user.SysUser;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.request.user.UserLoginRequest;
import com.lei.mes.request.user.UserSaveRequest;
import com.lei.mes.service.user.SysUserService;
import com.lei.mes.util.JwtUtils;
import com.lei.mes.util.UserContextHolder;
import com.lei.mes.vo.LoginResponse;
import com.lei.mes.util.UserContext;
import com.lei.mes.vo.user.UserVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 用户管理 Controller
 * @author lei
 */
@Slf4j
@RestController
@RequestMapping("/api/sys/user")
public class UserController {

    @Autowired
    private SysUserService sysUserService;


    /**
     * 获取当前登录用户
     * @return 当前登录用户信息
     */
     @GetMapping("/current")
     public Result<UserVO> getCurrentUser() {
        // 从拦截器设置的 ThreadLocal获取当前登录用户
        UserContext userContext =  UserContextHolder.getUserContext();
        UserVO vo = sysUserService.getUserVoById(userContext.getUserId());
        if (vo == null) {
            return Result.error(404, "当前登录用户不存在或已删除");
        }
        return Result.success(vo);
     }

    /**
     * 分页查询用户列表
     */
    @GetMapping("/list")
    public Result<IPage<UserVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String username) {

        IPage<SysUser> page = sysUserService.getUserPage(pageNum, pageSize, username);
        IPage<UserVO> voPage = page.convert(user -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(user, vo);
            return vo;
        });
        return Result.success(voPage);
    }

    /**
     * 根据 ID 查询用户详情
     */
    @GetMapping("/{id}")
    public Result<UserVO> getById(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        if (user == null) {
            return Result.error(404, "用户不存在或已删除");
        }
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return Result.success(vo);
    }

    /**
     * 新增用户
     */
    @PostMapping
    public Result<Void> add(@Valid @RequestBody UserSaveRequest request) {
        try {
            sysUserService.addUser(request);
            return Result.success("新增成功", null);
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 编辑用户
     */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody UserSaveRequest request) {
        try {
            sysUserService.updateUser(request);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            sysUserService.deleteUser(id);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 切换用户状态（启用/禁用）
     */
    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            sysUserService.changeStatus(id, status);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }
}

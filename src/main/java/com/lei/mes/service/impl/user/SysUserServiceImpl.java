package com.lei.mes.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mes.entity.user.SysUser;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.mapper.user.SysUserMapper;
import com.lei.mes.request.user.UserSaveRequest;
import com.lei.mes.service.user.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户表 Service 实现
 * @author lei
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    @Override
    public IPage<SysUser> getUserPage(int pageNum, int pageSize, String username) {
        // 构建分页对象
        Page<SysUser> page = new Page<>(pageNum, pageSize);

        // 构建查询条件（自动排除逻辑删除数据）
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(username)) {
            // 账号或姓名模糊匹配
            wrapper.and(w -> w
                    .like(SysUser::getUsername, username)
                    .or()
                    .like(SysUser::getRealName, username)
            );
        }
        // 按创建时间倒序
        wrapper.orderByDesc(SysUser::getCreatedAt);

        return this.page(page, wrapper);
    }

    @Override
    public void addUser(UserSaveRequest request) {
        // 检查用户名是否已存在
        long count = this.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, request.getUsername()));
        if (count > 0) {
            throw new BusinessException(400, "用户名已存在");
        }

        // Request → Entity
        SysUser user = new SysUser();
        BeanUtils.copyProperties(request, user);

        // 设置默认值
        user.setId(null);
        user.setStatus(1); // 默认启用
        // TODO: 密码 BCrypt 加密（登录模块完成后使用 BCryptPasswordEncoder）
        // user.setPassword(passwordEncoder.encode(request.getPassword()));

        this.save(user);
        log.info("新增用户成功: {}", request.getUsername());
    }

    @Override
    public void updateUser(UserSaveRequest request) {
        // 检查用户是否存在
        SysUser existing = this.getById(request.getId());
        if (existing == null) {
            throw new BusinessException(404, "用户不存在或已删除");
        }

        // 检查用户名是否被其他用户使用
        long count = this.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, request.getUsername())
                .ne(SysUser::getId, request.getId()));
        if (count > 0) {
            throw new BusinessException(400, "用户名已存在");
        }

        // Request → Entity（只更新前端传来的字段）
        SysUser update = new SysUser();
        BeanUtils.copyProperties(request, update);

        this.updateById(update);
        log.info("更新用户成功: ID={}", request.getId());
    }

    @Override
    public void deleteUser(Long id) {
        // 检查用户是否存在（逻辑删除后 getById 返回 null）
        SysUser existing = this.getById(id);
        if (existing == null) {
            throw new BusinessException(404, "用户不存在或已删除");
        }

        // 逻辑删除（MyBatis-Plus @TableLogic 自动转 UPDATE SET is_delete=1）
        this.removeById(id);
        log.info("删除用户成功: ID={}", id);
    }

    @Override
    public void changeStatus(Long id, Integer status) {
        // 检查用户是否存在
        SysUser existing = this.getById(id);
        if (existing == null) {
            throw new BusinessException(404, "用户不存在或已删除");
        }

        // 更新状态
        SysUser update = new SysUser();
        update.setId(id);
        update.setStatus(status);
        this.updateById(update);
        log.info("切换用户状态成功: ID={}, status={}", id, status);
    }
}

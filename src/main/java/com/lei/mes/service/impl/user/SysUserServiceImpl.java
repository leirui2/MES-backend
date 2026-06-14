package com.lei.mes.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mes.entity.user.SysUser;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.mapper.user.SysUserMapper;
import com.lei.mes.request.user.UserLoginRequest;
import com.lei.mes.request.user.UserSaveRequest;
import com.lei.mes.service.user.SysUserService;
import com.lei.mes.util.JwtUtils;
import com.lei.mes.vo.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.http.HttpRequest;

/**
 * 用户表 Service 实现
 * @author lei
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    //  JWT 工具类
    @Autowired
    private JwtUtils jwtUtils;

    //注入加盐算法
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
        // 状态默认启用
        user.setStatus(1);
        // 密码 BCrypt 加密
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
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

    /**
     * 用户登录
     * @param request 用户请求体
     * @return 登录响应
         */
    @Override
    public LoginResponse login(UserLoginRequest request) {
        // 检查用户是否存在
        SysUser existing = this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, request.getUsername()));
        if (existing == null) {
            throw new BusinessException(404, "当前登录用户不存在或已删除");
        }
        if (existing.getStatus() == 0) {
            throw new BusinessException(400, "当前登录用户已禁用");
        }

        // 密码校验（matches 方法内部处理盐值提取与比对）
        if (!bCryptPasswordEncoder.matches(request.getPassword(), existing.getPassword())) {
            throw new BusinessException(400, "当前登录用户密码错误");
        }
        String accessToken = jwtUtils.generateAccessToken(existing.getId(),existing.getUsername());
        String refreshToken = jwtUtils.generateRefreshToken(existing.getUsername());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setUserId(existing.getId());
        loginResponse.setUsername(existing.getUsername());
        loginResponse.setRealName(existing.getRealName());
        loginResponse.setEmail(existing.getEmail());
        loginResponse.setPhone(existing.getPhone());
        return loginResponse;
    }





}

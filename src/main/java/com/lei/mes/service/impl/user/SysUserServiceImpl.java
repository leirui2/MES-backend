package com.lei.mes.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mes.entity.user.SysUser;
import com.lei.mes.entity.user.SysUserRole;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.mapper.user.SysUserMapper;
import com.lei.mes.mapper.user.SysUserRoleMapper;
import com.lei.mes.request.user.UserLoginRequest;
import com.lei.mes.request.user.UserSaveRequest;
import com.lei.mes.service.user.SysUserService;
import com.lei.mes.util.JwtUtils;
import com.lei.mes.vo.user.LoginResponseVO;
import com.lei.mes.vo.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * 用户表 Service 实现
 * @author lei
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    // 构造函数注入 SysUserMapper 和 SysUserRoleMapper
    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;

    //  JWT 工具类
    @Autowired
    private JwtUtils jwtUtils;

    //注入加盐算法
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //final修饰必须 构造函数注入 SysUserMapper
    public SysUserServiceImpl(SysUserMapper sysUserMapper, SysUserRoleMapper sysUserRoleMapper) {
        this.sysUserMapper = sysUserMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
    }


    @Override
    public IPage<UserVO> getUserPage(int pageNum, int pageSize, String username) {
        Page<UserVO> page = new Page<>(pageNum, pageSize);
        return sysUserMapper.getUserVoPage(page, username);
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

        this.save(user);

        // 新增：角色绑定关系表中新增用户角色绑定关系
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            // 新增用户角色绑定关系
            sysUserRoleMapper.insertBatch(request.getRoleIds().stream()
                    .map(roleId -> new SysUserRole(user.getId(), roleId))
                    .collect(Collectors.toList()));
            log.info("新增用户角色绑定关系成功: ID={}", user.getId());
        }

        log.info("新增用户成功: {}", request.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

        // 如果传了新密码，加密后再存
        if (StringUtils.hasText(request.getPassword())) {
            update.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        } else {
            update.setPassword(existing.getPassword());
        }

        this.updateById(update);
        // 更新用户角色绑定关系

        // 更新角色绑定：先删后增
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, update.getId()));
        log.info("更新用户角色绑定关系成功: ID={}", update.getId());

        // 新增角色绑定
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            sysUserRoleMapper.insertBatch(request.getRoleIds().stream()
                    .map(roleId -> new SysUserRole(update.getId(), roleId))
                    .collect(Collectors.toList()));
            log.info("更新用户角色绑定关系成功: ID={}", update.getId());
        }

        log.info("更新用户成功: ID={}", request.getId());
    }

    @Override
    public void deleteUser(Long id) {
        // 检查用户是否存在（逻辑删除后 getById 返回 null）
        SysUser existing = this.getById(id);
        if (existing == null) {
            throw new BusinessException(404, "用户不存在或已删除");
        }

        // 检查sys_user_role表中用户是否已经绑定角色
//        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_id", id);
//        long roleCount = sysUserRoleMapper.selectCount(queryWrapper);
//        if (roleCount > 0) {
//            // 如果用户绑定角色，删除sys_user_role表中用户绑定的角色
//            sysUserRoleMapper.delete(queryWrapper);
//            log.info("删除用户绑定的角色成功: ID={}", id);
//        }
        // 逻辑删除（MyBatis-Plus @TableLogic 自动转 UPDATE SET is_delete=1）
        // 直接逻辑删除，角色绑定保留（不影响查询）
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
    public LoginResponseVO login(UserLoginRequest request) {
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
        LoginResponseVO loginResponseVO = new LoginResponseVO();
        loginResponseVO.setAccessToken(accessToken);
        loginResponseVO.setRefreshToken(refreshToken);
        UserVO userVO = sysUserMapper.getUserVoById(existing.getId());
        loginResponseVO.setUserId(userVO.getId());
        loginResponseVO.setUsername(userVO.getUsername());
        loginResponseVO.setRealName(userVO.getRealName());
        loginResponseVO.setEmail(userVO.getEmail());
        loginResponseVO.setPhone(userVO.getPhone());
        loginResponseVO.setRoles(userVO.getRoles());
        return loginResponseVO;
    }

    /**
     * 根据用户ID查询用户VO
     * @param userId 用户ID
     * @return 用户VO
     */
    @Override
    public UserVO getUserVoById(Long userId) {
        UserVO vo = sysUserMapper.getUserVoById(userId);
        if (vo == null) {
            throw new BusinessException(404, "当前登录用户不存在或已删除");
        }
        if (vo.getStatus() == 0 ){
            throw new BusinessException(400, "当前登录用户已禁用");
        }
        return vo;
    }


}

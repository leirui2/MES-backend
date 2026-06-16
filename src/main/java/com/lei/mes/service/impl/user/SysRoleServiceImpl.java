package com.lei.mes.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mes.entity.user.SysRole;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.request.user.RoleSaveRequest;
import com.lei.mes.service.user.SysRoleService;
import com.lei.mes.mapper.user.SysRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
* @author lei
* @description 针对表【sys_role(角色表)】的数据库操作Service实现
* @createDate 2026-06-16 16:23:20
*/
@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements SysRoleService{

    /**
     * 分页查询角色列表
     *
     * @param pageNum
     * @param pageSize
     * @param roleName
     */
    @Override
    public IPage<SysRole> getRolePage(int pageNum, int pageSize, String roleName) {
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        //构建查询条件
        LambdaQueryWrapper<SysRole> query = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(roleName)){
            query.like(SysRole::getRoleName, roleName);
        }
        // 按创建时间倒序
        query.orderByDesc(SysRole::getCreatedAt);
        return this.page(page, query);
    }

    /**
     * 新增角色
     *
     * @param request
     */
    @Override
    public void addRole(RoleSaveRequest request) {
        //检查是否存在
        SysRole existing = this.getOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, request.getRoleCode()));
        if(existing != null){
            throw new BusinessException(400, "角色编码已存在");
        }
        // 新增角色
        SysRole role = new SysRole();
        BeanUtils.copyProperties(request, role);
        role.setStatus(1);
        this.save(role);
        log.info("新增角色成功: ID={} , 角色名称={}", role.getId(), role.getRoleName());
    }

    /**
     * 更新角色
     *
     * @param request
     */
    @Override
    public void updateRole(RoleSaveRequest request) {
        // 检查角色是否存在
        SysRole existing = this.getById(request.getId());
        if(existing == null){
            throw new BusinessException(404, "角色不存在或已删除");
        }
        // 检查角色编码或名称是否已存在
        long count = this.count(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, request.getRoleCode())
                .or()
                .eq(SysRole::getRoleName, request.getRoleName()));
        if(count > 0){
            throw new BusinessException(400, "角色编码或名称已存在");
        }
        // 更新角色
        SysRole update = new SysRole();
        update.setId(request.getId());
        BeanUtils.copyProperties(request, update);
        this.updateById(update);
        log.info("更新角色成功: ID={} , 角色名称={}", request.getId(), request.getRoleName());
    }

    /**
     * 删除角色
     *
     * @param id
     */
    @Override
    public void deleteRole(Long id) {
        // 检查角色是否存在
        SysRole existing = this.getById(id);
        if(existing == null){
            throw new BusinessException(404, "角色不存在或已删除");
        }
        // 逻辑删除（MyBatis-Plus @TableLogic 自动转 UPDATE SET is_delete=1）
        this.removeById(id);
        log.info("删除角色成功: ID={}", id);
    }

    /**
     * 更新角色状态
     *
     * @param id
     * @param status
     */
    @Override
    public void changeStatus(Long id, Integer status) {
        // 检查角色是否存在
        SysRole existing = this.getById(id);
        if(existing == null){
            throw new BusinessException(404, "角色不存在或已删除");
        }
        // 更新状态
        SysRole update = new SysRole();
        update.setId(id);
        update.setStatus(status);
        this.updateById(update);
        log.info("切换角色状态成功: ID={}, status={}", id, status);
    }
}





package com.lei.mes.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mes.entity.user.SysUser;
import com.lei.mes.request.user.UserSaveRequest;

/**
 * 用户表 Service
 * @author lei
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查询用户列表
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param username 搜索关键词（账号/姓名模糊匹配）
     * @return 分页结果
     */
    IPage<SysUser> getUserPage(int pageNum, int pageSize, String username);

    /**
     * 新增用户
     * @param request 用户请求体
     */
    void addUser(UserSaveRequest request);

    /**
     * 编辑用户
     * @param request 用户请求体
     */
    void updateUser(UserSaveRequest request);

    /**
     * 删除用户（逻辑删除）
     * @param id 用户 ID
     */
    void deleteUser(Long id);

    /**
     * 切换用户状态（启用/禁用）
     * @param id     用户 ID
     * @param status 状态
     */
    void changeStatus(Long id, Integer status);
}

package com.lei.mes.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lei.mes.entity.user.SysUser;
import com.lei.mes.vo.user.UserVO;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表 Mapper
 * @author lei
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户ID查询用户VO
     *
     * @param userId 用户ID
     * @return 用户VO
     */
    UserVO getUserVoById(@Param("userId")Long userId);

    /**
     * 分页查询用户VO
     *
     * @param page 分页对象
     * @param username 用户名或姓名
     * @return 用户VO分页结果
     */
    //多个参数，需要使用@Param注解
    IPage<UserVO> getUserVoPage(Page<UserVO> page,@Param("username") String username);
}

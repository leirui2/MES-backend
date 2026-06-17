package com.lei.mes.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}

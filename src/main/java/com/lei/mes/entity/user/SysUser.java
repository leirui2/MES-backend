package com.lei.mes.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @author lei
 * @TableName sys_user
 */
@TableName(value ="sys_user")
@Data
public class SysUser implements Serializable {

    /**
     * 主键，分配 ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 登录账号
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码（BCrypt 加密）
     */
    @TableField(value = "password")
    private String password;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 头像 URL
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 状态：0-禁用 1-正常
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 部门 ID
     */
    @TableField(value = "dept_id")
    private Long deptId;

    /**
     * 最后登录时间
     */
    @TableField(value = "last_login_time")
    private Date lastLoginTime;

    /**
     * 最后登录 IP
     */
    @TableField(value = "last_login_ip")
    private String lastLoginIp;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private Date createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at")
    private Date updatedAt;

    /**
     * 逻辑删除：0-未删除 1-已删除（MyBatis-Plus @TableLogic 自动处理）
     */
    @TableLogic
    @TableField(value = "is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    // ========== 非数据库字段（用于接收前端参数） ==========

    /**
     * 确认密码（仅注册时使用）
     */
    @TableField(exist = false)
    private String confirmPassword;
}

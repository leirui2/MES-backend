package com.lei.mes.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 新增/编辑用户请求体
 * 使用 @Validated 做参数校验
 */
@Data
public class UserSaveRequest implements Serializable {

    /**
     * 用户ID（编辑时必填，新增时前端不传或传 null）
     */
    private Long id;

    /**
     * 登录账号（必填，3-20位字母数字下划线）
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度 3-20 位")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字、下划线")
    private String username;

    /**
     * 密码（新增时必填，至少6位）
     */
    @Size(min = 6, max = 20, message = "密码长度 6-20 位")
    private String password;

    /**
     * 真实姓名（必填）
     */
    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "姓名最多 50 字符")
    private String realName;

    /**
     * 手机号（可选，但传了要符合格式）
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 邮箱（可选，但传了要符合格式）
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 部门ID
     */
    private Long deptId;

    private static final long serialVersionUID = 1L;
}

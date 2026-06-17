package com.lei.mes.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 新增/编辑用户请求体
 * 与数据库实体解耦，按需接收字段
 * @author lei
 */
@Data
public class UserSaveRequest implements Serializable {

    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度必须在4-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "密码只能包含字母、数字和下划线")
    private String password;
    @NotBlank(message = "真实姓名不能为空")
    @Size(min = 2, max = 10, message = "真实姓名长度必须在2-10个字符之间")
    private String realName;

    private List<Long> roleIds;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Email(message = "邮箱格式错误")
    private String email;
    private String avatar;
    private Long deptId;

    @Serial
    private static final long serialVersionUID = 1L;
}

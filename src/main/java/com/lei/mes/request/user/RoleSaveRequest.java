package com.lei.mes.request.user;

import com.baomidou.mybatisplus.annotation.TableField;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 新增/编辑角色请求体
 * @author lei
 */
@Data
public class RoleSaveRequest implements Serializable {
    /**
     * 主键，自增
     */
    private Long id;

    @NotBlank(message = "角色名称不能为空")
    @Size(min = 2, max = 20, message = "角色名称长度必须在2-20个字符之间")
    private String roleName;

    /**
     * 角色编码（ADMIN/OPERATOR/QC等）
     */
    @NotBlank(message = "角色编码不能为空")
    @Size(min = 2, max = 20, message = "角色编码长度必须在2-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "角色编码只能包含字母、数字和下划线")
    private String roleCode;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    @Size(min = 2, max = 50, message = "描述长度必须在2-50个字符之间")
    private String description;


    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
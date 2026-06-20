package com.lei.mes.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * 菜单表
 * @author lei
 */
@TableName(value = "sys_menu")
@Data
public class SysMenu implements Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "parent_id")
    private Long parentId;  // 父菜单 ID，0 表示顶级

    @TableField(value = "menu_name")
    private String menuName;

    @TableField(value = "menu_code")
    private String menuCode;

    @TableField(value = "menu_type")
    private Integer menuType;  // 1-目录 2-菜单 3-按钮

    @TableField(value = "url")
    private String url;

    @TableField(value = "icon")
    private String icon;

    @TableField(value = "sort_order")
    private Integer sortOrder;

    @TableField(value = "perms")
    private String perms;  // 权限标识

    @TableField(value = "status")
    private Integer status;  // 0-隐藏 1-显示

    @TableField(value = "created_at")
    private java.util.Date createdAt;

    @TableField(value = "updated_at")
    private java.util.Date updatedAt;

    @TableField(value = "is_delete")
    private Integer isDelete;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}

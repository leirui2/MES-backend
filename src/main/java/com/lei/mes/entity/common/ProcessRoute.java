package com.lei.mes.entity.common;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 工艺路线表实体类
 * @author lei
 */
@TableName(value = "process_route")
@Data
public class ProcessRoute implements Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "route_name")
    private String routeName;

    @TableField(value = "product_id")
    private Long productId;

    @TableField(value = "status")
    private Integer status;  // 0-停用 1-启用

    @TableField(value = "created_at")
    private Date createdAt;

    @TableField(value = "updated_at")
    private Date updatedAt;

    @TableField(value = "is_delete")
    private Integer isDelete;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}

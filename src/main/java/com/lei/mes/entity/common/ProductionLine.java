package com.lei.mes.entity.common;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 产线表
 * @author lei
 */
@TableName(value = "production_line")
@Data
public class ProductionLine implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "line_code")
    private String lineCode;

    @TableField(value = "line_name")
    private String lineName;

    @TableField(value = "workshop_id")
    private Long workshopId;

    @TableField(value = "capacity_per_day")
    private Integer capacityPerDay;

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

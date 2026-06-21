package com.lei.mes.entity.common;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 车间表
 * @author lei
 */
@TableName(value = "workshop")
@Data
public class Workshop implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "workshop_code")
    private String workshopCode;

    @TableField(value = "workshop_name")
    private String workshopName;

    @TableField(value = "location")
    private String location;

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

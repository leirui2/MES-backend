package com.lei.mes.entity.common;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 工位表
 * @author lei
 */
@TableName(value = "workstation")
@Data
public class Workstation implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "station_code")
    private String stationCode;

    @TableField(value = "station_name")
    private String stationName;

    @TableField(value = "line_id")
    private Long lineId;

    @TableField(value = "station_type")
    private String stationType;

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

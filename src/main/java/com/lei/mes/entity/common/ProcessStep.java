package com.lei.mes.entity.common;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 工序表实体类
 * @author lei
 */
@TableName(value = "process_step")
@Data
public class ProcessStep implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "route_id")
    private Long routeId;

    @TableField(value = "step_code")
    private String stepCode;

    @TableField(value = "step_name")
    private String stepName;

    @TableField(value = "workstation_id")
    private Long workstationId;

    @TableField(value = "sort_order")
    private Integer sortOrder;

    @TableField(value = "standard_time")
    private BigDecimal standardTime;

    @TableField(value = "qc_required")
    private Integer qcRequired;  // 0-否 1-是

    @TableField(value = "is_delete")
    private Integer isDelete;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}

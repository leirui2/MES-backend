package com.lei.mes.entity.common;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 物料清单项
 * @author lei
 */
@TableName(value = "bom_item")
@Data
public class BomItem implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "bom_id")
    private Long bomId;

    @TableField(value = "material_id")
    private Long materialId;

    @TableField(value = "material_code")
    private String materialCode;

    @TableField(value = "material_name")
    private String materialName;

    @TableField(value = "quantity")
    private BigDecimal quantity;

    @TableField(value = "loss_rate")  // ← 新增
    private BigDecimal lossRate;

    @TableField(value = "sort_order")  // ← 新增
    private Integer sortOrder;

    @TableField(value = "is_delete")
    private Integer isDelete;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}

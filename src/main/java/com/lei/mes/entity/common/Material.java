package com.lei.mes.entity.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 物料表
 * @author lei
 */
@TableName(value ="material")
@Data
public class Material implements Serializable {
    /**
     * 主键，自增
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 物料编号，唯一
     */
    @TableField(value = "material_code")
    private String materialCode;

    /**
     * 物料名称
     */
    @TableField(value = "material_name")
    private String materialName;

    /**
     * 规格型号
     */
    @TableField(value = "specification")
    private String specification;

    /**
     * 计量单位
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * 物料分类（芯片/外壳/线材/辅料）
     */
    @TableField(value = "category")
    private String category;

    /**
     * 当前库存数量
     */
    @TableField(value = "stock_qty")
    private BigDecimal stockQty;

    /**
     * 安全库存下限
     */
    @TableField(value = "min_stock")
    private BigDecimal minStock;

    /**
     * 单价
     */
    @TableField(value = "unit_price")
    private BigDecimal unitPrice;

    /**
     * 状态：0-停用 1-启用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private Date createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at")
    private Date updatedAt;

    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
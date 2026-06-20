package com.lei.mes.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 产品表
 * @author lei
 * @TableName product
 */
@TableName(value ="product")
@Data
public class Product implements Serializable {
    /**
     * 主键，自增
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 产品编号，唯一
     */
    @TableField(value = "product_code")
    private String productCode;

    /**
     * 产品名称
     */
    @TableField(value = "product_name")
    private String productName;

    /**
     * 规格型号
     */
    @TableField(value = "specification")
    private String specification;

    /**
     * 计量单位（个/箱/件）
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * 产品分类（快充/普通/无线）
     */
    @TableField(value = "category")
    private String category;

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
package com.lei.mes.request.product;

import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 新增产品请求体
 * @author lei
 */
@Data
public class ProductSaveRequest implements Serializable {
    /**
     * 主键，自增
     */
    private Long id;

    /**
     * 产品编号，唯一
     */
    private String productCode;

    /**
     * 产品名称，唯一
     */
    @NotBlank(message = "产品名称不能为空")
    @Size(min = 2, max = 50, message = "产品名称长度必须在2-50个字符之间")
    private String productName;

    /**
     * 规格型号
     */
    @NotBlank(message = "规格型号不能为空")
    @Size(min = 2, max = 20, message = "规格型号长度必须在2-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "规格型号只能包含字母、数字和横横线")
    private String specification;

    /**
     * 计量单位（个/箱/件）
     */
    @NotBlank(message = "计量单位不能为空")
    @Size(min = 1, max = 10, message = "计量单位长度必须在1-10个字符之间")
    private String unit;

    /**
     * 产品分类（快充/普通/无线）
     */
    @NotBlank(message = "产品分类不能为空")
    @Size(min = 2, max = 20, message = "产品分类长度必须在2-20个字符之间")
    private String category;


    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
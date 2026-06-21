package com.lei.mes.request.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * BOM 明细请求
 * @author lei
 */
@Data
public class BomItemRequest implements Serializable {
    private Long id;

    @NotNull(message = "物料不能为空")
    private Long materialId;

    @NotBlank(message = "物料编号不能为空")
    private String materialCode;

    @NotBlank(message = "物料名称不能为空")
    private String materialName;

    private String specification;

    @NotNull(message = "用量不能为空")
    private BigDecimal quantity;

    private BigDecimal lossRate;      // ← 新增
    private Integer sortOrder;        // ← 新增

    @Serial
    private static final long serialVersionUID = 1L;
}
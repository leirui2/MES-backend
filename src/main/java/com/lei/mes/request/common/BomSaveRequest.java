package com.lei.mes.request.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 保存 BOM 请求
 * @author lei
 */
@Data
public class BomSaveRequest implements Serializable {

    private Long id;

    @NotNull(message = "产品不能为空")
    private Long productId;

    @NotBlank(message = "产品编号不能为空")
    private String productCode;

    @NotBlank(message = "版本号不能为空")
    private String version;

    // BOM 明细列表
    private List<BomItemRequest> items;


    @Serial
    private static final long serialVersionUID = 1L;
}

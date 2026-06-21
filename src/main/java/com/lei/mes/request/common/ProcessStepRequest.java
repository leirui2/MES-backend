package com.lei.mes.request.common;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 工序请求参数
 * @author lei
 */
@Data
public class ProcessStepRequest implements Serializable {
    private Long id;

    @NotBlank(message = "工序编号不能为空")
    private String stepCode;

    @NotBlank(message = "工序名称不能为空")
    private String stepName;

    private Long workstationId;

    private Integer sortOrder;

    private BigDecimal standardTime;

    private Integer qcRequired;  // 0-否 1-是

    @Serial
    private static final long serialVersionUID = 1L;
}


package com.lei.mes.request.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

/**
 * 生产线请求
 * @author lei
 */
@Data
public class ProductionLineSaveRequest implements Serializable {

    private Long id;

    @NotBlank(message = "产线编号不能为空")
    private String lineCode;

    @NotBlank(message = "产线名称不能为空")
    private String lineName;

    @NotNull(message = "车间不能为空")
    private Long workshopId;

    private Integer capacityPerDay;

    private Integer status;

    @Serial
    private static final long serialVersionUID = 1L;
}

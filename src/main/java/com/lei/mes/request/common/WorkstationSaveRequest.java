package com.lei.mes.request.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

/**
 * 工位保存请求
 * @author lei
 */
@Data
public class WorkstationSaveRequest implements Serializable {

    private Long id;

    @NotBlank(message = "工位编号不能为空")
    private String stationCode;

    @NotBlank(message = "工位名称不能为空")
    private String stationName;

    @NotNull(message = "产线不能为空")
    private Long lineId;

    private String stationType;

    private Integer status;

    @Serial
    private static final long serialVersionUID = 1L;
}

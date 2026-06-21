package com.lei.mes.request.common;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

/**
 * 车间请求体
 * @author lei
 */
@Data
public class WorkshopSaveRequest implements Serializable {

    private Long id;

    @NotBlank(message = "车间编号不能为空")
    private String workshopCode;

    @NotBlank(message = "车间名称不能为空")
    private String workshopName;

    private String location;

    private Integer status;

    @Serial
    private static final long serialVersionUID = 1L;
}

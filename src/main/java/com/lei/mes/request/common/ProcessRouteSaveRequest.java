package com.lei.mes.request.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 工艺路线保存请求参数
 * @author lei
 */
@Data
public class ProcessRouteSaveRequest implements Serializable {

    private Long id;

    @NotBlank(message = "工艺路线名称不能为空")
    private String routeName;

    @NotNull(message = "产品不能为空")
    private Long productId;

    private Integer status;  // 默认启用

    // 工序列表
    private List<ProcessStepRequest> steps;


    @Serial
    private static final long serialVersionUID = 1L;
}

package com.lei.mes.request.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 物料保存请求体
 * @author lei
 */
@Data
public class MaterialSaveRequest implements Serializable {

    private Long id;

    @NotBlank(message = "物料编号不能为空")
    @Size(max = 20, message = "物料编号最多20个字符")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "物料编号只能包含字母、数字、短横线和下划线")
    private String materialCode;

    @NotBlank(message = "物料名称不能为空")
    @Size(max = 20, message = "物料名称最多20个字符")
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5_-]+$", message = "物料名称只能包含字母、数字、短横线、下划线和中文-")
    private String materialName;

    private String specification;

    @NotBlank(message = "计量单位不能为空")
    @Size(min = 1, max = 5, message = "计量单位必须在1到最多为5个字符之间")
    private String unit;

    private String category;


    @Serial
    private static final long serialVersionUID = 1L;
}

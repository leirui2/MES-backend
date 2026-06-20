package com.lei.mes.vo.common;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;

/**
 * @author lei
 */
@Data
public class MaterialVO implements Serializable {
    private Long id;
    private String materialCode;
    private String materialName;
    private String specification;
    private String unit;
    private String category;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
    // 新增：预警状态
    private Integer warningLevel;  // 0-正常 1-预警 2-缺货
    private String warningMsg;     // 预警描述
}

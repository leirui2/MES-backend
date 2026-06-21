package com.lei.mes.vo.common;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工艺路线 VO
 * @author lei
 */
@Data
public class ProcessRouteVO implements Serializable {
    private Long id;
    private String routeName;
    private Long productId;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;

    // 工序列表
    private List<ProcessStepVO> steps;
}

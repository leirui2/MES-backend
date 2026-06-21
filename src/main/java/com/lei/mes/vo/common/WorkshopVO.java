package com.lei.mes.vo.common;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class WorkshopVO implements Serializable {
    private Long id;
    private String workshopCode;
    private String workshopName;
    private String location;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;

    // 关联的产线列表
    private List<ProductionLineVO> lines;
}

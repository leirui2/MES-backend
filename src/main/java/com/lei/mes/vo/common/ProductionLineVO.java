package com.lei.mes.vo.common;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ProductionLineVO implements Serializable {
    private Long id;
    private String lineCode;
    private String lineName;
    private Long workshopId;
    private String workshopName;
    private Integer capacityPerDay;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;

    // 关联的工位列表
    private List<WorkstationVO> workstations;
}

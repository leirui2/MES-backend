package com.lei.mes.vo.common;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class WorkstationVO implements Serializable {
    private Long id;
    private String stationCode;
    private String stationName;
    private Long lineId;
    private String lineName;
    private String stationType;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
}

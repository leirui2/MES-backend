package com.lei.mes.vo.common;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lei
 */
@Data
public class ProcessStepVO implements Serializable {
    private Long id;
    private Long routeId;
    private String stepCode;
    private String stepName;
    private Long workstationId;
    private Integer sortOrder;
    private BigDecimal standardTime;
    private Integer qcRequired;
}

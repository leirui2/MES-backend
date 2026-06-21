package com.lei.mes.vo.common;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * BOM 明细VO
 * @author lei
 */
@Data
public class BomItemVO implements Serializable {
    private Long id;
    private Long bomId;
    private Long materialId;
    private String materialCode;
    private String materialName;
    private BigDecimal quantity;
    private BigDecimal lossRate;
    private Integer sortOrder;
}

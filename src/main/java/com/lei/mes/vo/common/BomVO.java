package com.lei.mes.vo.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Bom VO
 * @author lei
 */
@Data
public class BomVO implements Serializable {
    private Long id;
    private String productCode;
    private Long productId;
    private String version;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;

    // BOM 明细列表
    private List<BomItemVO> items;
}

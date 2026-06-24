package com.lei.mes.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class SysOperationLogVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private String username;
    private String operation;
    private String method;
    private String params;
    private String result;
    private String ip;
    private Integer duration;
    private Integer status;
    private String errorMsg;
    private Date createdAt;
}

package com.lei.mes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mes.entity.SysOperationLog;
import com.lei.mes.vo.SysOperationLogVO;

public interface SysOperationLogService extends IService<SysOperationLog> {

    /**
     * 分页查询操作日志
     */
    IPage<SysOperationLogVO> getOperationLogPage(int pageNum, int pageSize,
                                                 Long userId, String operation,
                                                 String startTime, String endTime);
}

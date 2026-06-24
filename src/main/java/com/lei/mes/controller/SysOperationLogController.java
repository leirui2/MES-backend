package com.lei.mes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lei.mes.common.Result;
import com.lei.mes.service.SysOperationLogService;
import com.lei.mes.vo.SysOperationLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system/operation-log")
public class SysOperationLogController {

    @Autowired
    private SysOperationLogService operationLogService;

    /**
     * 分页查询操作日志
     */
    @GetMapping("/list")
    public Result<IPage<SysOperationLogVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {

        IPage<SysOperationLogVO> page = operationLogService.getOperationLogPage(
                pageNum, pageSize, userId, operation, startTime, endTime);
        return Result.success(page);
    }
}

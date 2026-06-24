package com.lei.mes.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mes.entity.SysOperationLog;
import com.lei.mes.mapper.SysOperationLogMapper;
import com.lei.mes.service.SysOperationLogService;
import com.lei.mes.vo.SysOperationLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog>
        implements SysOperationLogService {

    @Autowired
    private SysOperationLogMapper sysOperationLogMapper;

    @Override
    public IPage<SysOperationLogVO> getOperationLogPage(int pageNum, int pageSize,
                                                        Long userId, String operation,
                                                        String startTime, String endTime) {
        Page<SysOperationLog> page = new Page<>(pageNum, pageSize);
        return sysOperationLogMapper.selectLogPage(page, userId, operation, startTime, endTime);
    }
}

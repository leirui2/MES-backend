package com.lei.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lei.mes.entity.SysOperationLog;
import com.lei.mes.vo.SysOperationLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysOperationLogMapper extends BaseMapper<SysOperationLog> {

    /**
     * 分页查询操作日志
     */
    IPage<SysOperationLogVO> selectLogPage(Page<SysOperationLog> page,
                                           @Param("userId") Long userId,
                                           @Param("operation") String operation,
                                           @Param("startTime") String startTime,
                                           @Param("endTime") String endTime);
}

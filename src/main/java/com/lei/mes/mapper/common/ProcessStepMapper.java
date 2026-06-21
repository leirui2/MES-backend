package com.lei.mes.mapper.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lei.mes.entity.common.ProcessStep;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProcessStepMapper extends BaseMapper<ProcessStep> {

    /**
     * 根据工艺路线 ID 查询工序列表
     */
    List<ProcessStep> selectByRouteId(@Param("routeId") Long routeId);

    /**
     * 批量插入工序
     */
    void insertBatch(@Param("list") List<ProcessStep> list);
}

package com.lei.mes.mapper.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lei.mes.entity.common.ProcessRoute;
import com.lei.mes.vo.common.ProcessRouteVO;
import org.apache.ibatis.annotations.Param;

public interface ProcessRouteMapper extends BaseMapper<ProcessRoute> {

    /**
     * 分页查询工艺路线
     */
    IPage<ProcessRouteVO> getRoutePage(Page<ProcessRoute> page,
                                       @Param("routeName") String routeName,
                                       @Param("productId") Long productId);

    /**
     * 查询工艺路线详情（含工序）
     */
    ProcessRouteVO getRouteDetail(@Param("id") Long id);

    /**
     * 按产品查询工艺路线
     */
    ProcessRouteVO getRouteByProduct(@Param("productId") Long productId);
}

package com.lei.mes.service.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mes.entity.common.ProcessRoute;
import com.lei.mes.request.common.ProcessRouteSaveRequest;
import com.lei.mes.vo.common.ProcessRouteVO;

public interface ProcessRouteService extends IService<ProcessRoute> {

    /**
     * 分页查询工艺路线
     */
    IPage<ProcessRouteVO> getRoutePage(int pageNum, int pageSize,
                                       String routeName, Long productId);

    /**
     * 查询工艺路线详情（含工序）
     */
    ProcessRouteVO getRouteDetail(Long id);

    /**
     * 新增工艺路线（含工序）
     */
    void addRoute(ProcessRouteSaveRequest request);

    /**
     * 更新工艺路线（含工序）
     */
    void updateRoute(ProcessRouteSaveRequest request);

    /**
     * 删除工艺路线
     */
    void deleteRoute(Long id);

    /**
     * 按产品查询工艺路线
     */
    ProcessRouteVO getRouteByProduct(Long productId);
}

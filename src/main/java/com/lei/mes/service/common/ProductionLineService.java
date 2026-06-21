package com.lei.mes.service.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mes.entity.common.ProductionLine;
import com.lei.mes.request.common.ProductionLineSaveRequest;
import com.lei.mes.vo.common.ProductionLineVO;

import java.util.List;

public interface ProductionLineService extends IService<ProductionLine> {
    
    /**
     * 分页查询产线
     */
    IPage<ProductionLineVO> getLinePage(int pageNum, int pageSize, 
                                         String lineName, Long workshopId);
    
    /**
     * 查询产线详情（含工位）
     */
    ProductionLineVO getLineDetail(Long id);
    
    /**
     * 新增产线
     */
    void addLine(ProductionLineSaveRequest request);
    
    /**
     * 编辑产线
     */
    void updateLine(ProductionLineSaveRequest request);
    
    /**
     * 删除产线
     */
    void deleteLine(Long id);
    
    /**
     * 查询所有产线（下拉选择用）
     */
    List<ProductionLineVO> getAllLines();
}

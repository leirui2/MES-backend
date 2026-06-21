package com.lei.mes.mapper.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lei.mes.entity.common.ProductionLine;
import com.lei.mes.vo.common.ProductionLineVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductionLineMapper extends BaseMapper<ProductionLine> {

    /**
     * 分页查询产线
     */
    IPage<ProductionLineVO> getLinePage(Page<ProductionLine> page,
                                        @Param("lineName") String lineName,
                                        @Param("workshopId") Long workshopId);

    /**
     * 查询产线详情（含工位）
     */
    ProductionLineVO getLineDetail(@Param("id") Long id);

    /**
     * 查询所有产线（下拉选择用）
     */
    List<ProductionLineVO> getAllLines();
}

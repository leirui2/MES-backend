package com.lei.mes.mapper.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lei.mes.entity.common.Workshop;
import com.lei.mes.vo.common.WorkshopVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkshopMapper extends BaseMapper<Workshop> {
    
    /**
     * 分页查询车间
     */
    IPage<WorkshopVO> getWorkshopPage(Page<Workshop> page, 
                                       @Param("workshopName") String workshopName);
    
    /**
     * 查询车间详情（含产线）
     */
    WorkshopVO getWorkshopDetail(@Param("id") Long id);
    
    /**
     * 查询所有车间（下拉选择用）
     */
    List<WorkshopVO> getAllWorkshops();
}

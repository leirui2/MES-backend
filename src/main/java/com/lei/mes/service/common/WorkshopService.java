package com.lei.mes.service.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mes.entity.common.Workshop;
import com.lei.mes.request.common.WorkshopSaveRequest;
import com.lei.mes.vo.common.WorkshopVO;

import java.util.List;

public interface WorkshopService extends IService<Workshop> {
    
    /**
     * 分页查询车间
     */
    IPage<WorkshopVO> getWorkshopPage(int pageNum, int pageSize, String workshopName);
    
    /**
     * 查询车间详情（含产线）
     */
    WorkshopVO getWorkshopDetail(Long id);
    
    /**
     * 新增车间
     */
    void addWorkshop(WorkshopSaveRequest request);
    
    /**
     * 编辑车间
     */
    void updateWorkshop(WorkshopSaveRequest request);
    
    /**
     * 删除车间
     */
    void deleteWorkshop(Long id);
    
    /**
     * 查询所有车间（下拉选择用）
     */
    List<WorkshopVO> getAllWorkshops();
}

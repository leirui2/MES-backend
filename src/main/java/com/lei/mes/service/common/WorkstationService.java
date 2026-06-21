package com.lei.mes.service.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mes.entity.common.Workstation;
import com.lei.mes.request.common.WorkstationSaveRequest;
import com.lei.mes.vo.common.WorkstationVO;

import java.util.List;

public interface WorkstationService extends IService<Workstation> {
    
    /**
     * 分页查询工位
     */
    IPage<WorkstationVO> getStationPage(int pageNum, int pageSize, 
                                         String stationName, Long lineId);
    
    /**
     * 查询工位详情
     */
    WorkstationVO getStationDetail(Long id);
    
    /**
     * 新增工位
     */
    void addStation(WorkstationSaveRequest request);
    
    /**
     * 编辑工位
     */
    void updateStation(WorkstationSaveRequest request);
    
    /**
     * 删除工位
     */
    void deleteStation(Long id);
    
    /**
     * 查询所有工位（下拉选择用）
     */
    List<WorkstationVO> getAllStations();
}

package com.lei.mes.mapper.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lei.mes.entity.common.Workstation;
import com.lei.mes.vo.common.WorkstationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkstationMapper extends BaseMapper<Workstation> {

    /**
     * 分页查询工位
     */
    IPage<WorkstationVO> getStationPage(Page<Workstation> page,
                                        @Param("stationName") String stationName,
                                        @Param("lineId") Long lineId);

    /**
     * 查询工位详情
     */
    WorkstationVO getStationDetail(@Param("id") Long id);

    /**
     * 查询所有工位（下拉选择用）
     */
    List<WorkstationVO> getAllStations();
}

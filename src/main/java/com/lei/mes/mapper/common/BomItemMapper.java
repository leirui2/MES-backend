package com.lei.mes.mapper.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lei.mes.entity.common.BomItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BomItemMapper extends BaseMapper<BomItem> {

    /**
     * 根据 BOM ID 查询明细列表
     */
    List<BomItem> selectByBomId(@Param("bomId") Long bomId);

    /**
     * 批量插入明细
     */
    void insertBatch(@Param("list") List<BomItem> list);
}

package com.lei.mes.mapper.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lei.mes.entity.common.Bom;
import com.lei.mes.vo.common.BomVO;
import org.apache.ibatis.annotations.Param;

public interface BomMapper extends BaseMapper<Bom> {

    /**
     * 分页查询 BOM（含产品名）
     */
    IPage<BomVO> getBomPage(Page<Bom> page,
                            @Param("productCode") String productCode,
                            @Param("version") String version,
                            @Param("status") Integer status);

    /**
     * 查询 BOM 详情（含明细）
     */
    BomVO getBomDetail(@Param("id") Long id);
}

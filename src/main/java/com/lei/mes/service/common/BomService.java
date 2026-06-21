package com.lei.mes.service.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mes.entity.common.Bom;
import com.lei.mes.request.common.BomSaveRequest;
import com.lei.mes.vo.common.BomVO;

public interface BomService extends IService<Bom> {

    /**
     * 分页查询 BOM
     */
    IPage<BomVO> getBomPage(int pageNum, int pageSize,
                            String productName, String version, Integer status);

    /**
     * 查询 BOM 详情（含明细）
     */
    BomVO getBomDetail(Long id);

    /**
     * 新增 BOM（含明细）
     */
    void addBom(BomSaveRequest request);

    /**
     * 更新 BOM（含明细）
     */
    void updateBom(BomSaveRequest request);

    /**
     * 删除 BOM
     */
    void deleteBom(Long id);

    /**
     * 变更 BOM 状态（草稿→生效→作废）
     */
    void changeStatus(Long id, Integer status);
}

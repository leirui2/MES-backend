package com.lei.mes.service.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mes.entity.common.Material;
import com.lei.mes.request.common.MaterialSaveRequest;
import com.lei.mes.vo.common.MaterialVO;

public interface MaterialService extends IService<Material> {

    /**
     * 分页查询物料
     */
    IPage<MaterialVO> getMaterialPage(int pageNum, int pageSize,
                                      String materialName, String category);

    /**
     * 新增物料
     */
    void addMaterial(MaterialSaveRequest request);

    /**
     * 更新物料
     */
    void updateMaterial(MaterialSaveRequest request);

    /**
     * 删除物料
     */
    void deleteMaterial(Long id);

    void changeStatus(Long id, Integer status);
}

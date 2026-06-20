package com.lei.mes.service.impl.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mes.entity.common.Material;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.mapper.common.MaterialMapper;
import com.lei.mes.request.common.MaterialSaveRequest;
import com.lei.mes.service.common.MaterialService;
import com.lei.mes.vo.common.MaterialVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * 物料服务实现类
 * @author lei
 */
@Slf4j
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material>
        implements MaterialService {

    @Autowired
    private MaterialMapper materialMapper;

    @Override
    public IPage<MaterialVO> getMaterialPage(int pageNum, int pageSize,
                                             String materialName, String category) {
        Page<Material> page = new Page<>(pageNum, pageSize);
        IPage<Material> result = this.page(page, new LambdaQueryWrapper<Material>()
                .eq(Material::getIsDelete, 0)
                .like(StringUtils.hasText(materialName), Material::getMaterialName, materialName)
                .eq(StringUtils.hasText(category), Material::getCategory, category)
                .orderByDesc(Material::getCreatedAt));
        // 转换 VO 并计算预警
        return result.convert(item -> {
            MaterialVO vo = new MaterialVO();
            BeanUtils.copyProperties(item, vo);

            // 计算预警级别
            if (item.getStockQty().compareTo(BigDecimal.ZERO) < 0) {
                vo.setWarningLevel(2);  // 缺货
                vo.setWarningMsg("库存为负，请及时处理");
            } else if (item.getStockQty().compareTo(item.getMinStock()) <= 0) {
                vo.setWarningLevel(1);  // 预警
                vo.setWarningMsg("库存不足，当前: " + item.getStockQty() + ", 安全库存: " + item.getMinStock());
            } else {
                vo.setWarningLevel(0);  // 正常
                vo.setWarningMsg("库存正常");
            }
            return vo;
        });
    }

    @Override
    public void addMaterial(MaterialSaveRequest request) {
        // 检查编号是否重复
        long count = this.count(new LambdaQueryWrapper<Material>()
                .eq(Material::getMaterialCode, request.getMaterialCode()));
        if (count > 0) {
            throw new BusinessException(400, "物料编号已存在");
        }

        Material material = new Material();
        BeanUtils.copyProperties(request, material);
        material.setStatus(1);
        this.save(material);
        log.info("新增物料成功: ID={}, 名称={}", material.getId(), material.getMaterialName());
    }

    @Override
    public void updateMaterial(MaterialSaveRequest request) {

        Material existing = this.getById(request.getId());
        if (existing == null) {
            throw new BusinessException(404, "物料不存在或已删除");
        }

        // 检查编号是否重复（排除自己）
        long count = this.count(new LambdaQueryWrapper<Material>()
                .eq(Material::getMaterialCode, request.getMaterialCode())
                .ne(Material::getId, request.getId()));
        if (count > 0) {
            throw new BusinessException(400, "物料编号已存在");
        }

        Material update = new Material();
        BeanUtils.copyProperties(request, update);

        // 只更新非空字段
        if (!StringUtils.hasText(request.getSpecification())) {
            update.setSpecification(null);
        }
        if (!StringUtils.hasText(request.getCategory())) {
            update.setCategory(null);
        }

        this.updateById(update);
        log.info("更新物料成功: ID={}", request.getId());
    }

    @Override
    public void deleteMaterial(Long id) {
        Material existing = this.getById(id);
        if (existing == null) {
            throw new BusinessException(404, "物料不存在或已删除");
        }
        this.removeById(id);
        log.info("删除物料成功: ID={}", id);
    }

    /**
     * @param id 物料 ID
     * @param status 状态 1: 启用, 0: 禁用
     */
    @Override
    public void changeStatus(Long id, Integer status) {
        Material existing = this.getById(id);
        if (existing == null) {
            throw new BusinessException(404, "物料不存在或已删除");
        }
        // 检查状态是否有效
        if(status != 1 && status != 0){
            throw new BusinessException(400, "状态无效，只能是1或0");
        }
        existing.setStatus(status);
        this.updateById(existing);
        log.info("切换物料状态成功: ID={}, 状态={}", id, status);
    }


}

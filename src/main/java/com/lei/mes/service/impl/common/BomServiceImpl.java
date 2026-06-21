package com.lei.mes.service.impl.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mes.entity.common.Bom;
import com.lei.mes.entity.common.BomItem;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.mapper.common.BomItemMapper;
import com.lei.mes.mapper.common.BomMapper;
import com.lei.mes.request.common.BomSaveRequest;
import com.lei.mes.service.common.BomService;
import com.lei.mes.vo.common.BomItemVO;
import com.lei.mes.vo.common.BomVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BomServiceImpl extends ServiceImpl<BomMapper, Bom> implements BomService {

    @Autowired
    private BomItemMapper bomItemMapper;

    @Override
    public IPage<BomVO> getBomPage(int pageNum, int pageSize,
                                   String productCode, String version, Integer status) {
        Page<Bom> page = new Page<>(pageNum, pageSize);
        return baseMapper.getBomPage(page, productCode, version, status);
    }

    @Override
    public BomVO getBomDetail(Long id) {
        BomVO bomVO = baseMapper.getBomDetail(id);
        if (bomVO == null) {
            throw new BusinessException(404, "BOM不存在或已删除");
        }
        return bomVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBom(BomSaveRequest request) {
        Bom bom = new Bom();
        bom.setProductId(request.getProductId());
        bom.setProductCode(request.getProductCode());
        bom.setVersion(request.getVersion());
        bom.setStatus(0);
        this.save(bom);

        // 保存明细
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            List<BomItem> items = request.getItems().stream()
                    .map(item -> {
                        BomItem bomItem = new BomItem();
                        bomItem.setBomId(bom.getId());
                        bomItem.setMaterialId(item.getMaterialId());
                        bomItem.setMaterialCode(item.getMaterialCode());
                        bomItem.setMaterialName(item.getMaterialName());
                        // ← 删除：specification、unit（BomItem 里没有这两个字段）
                        bomItem.setQuantity(item.getQuantity());
                        bomItem.setLossRate(item.getLossRate());
                        bomItem.setSortOrder(item.getSortOrder());
                        return bomItem;
                    })
                    .collect(Collectors.toList());

            bomItemMapper.insertBatch(items);
        }

        log.info("新增 BOM 成功: ID={}, 明细数={}",
                bom.getId(), request.getItems() != null ? request.getItems().size() : 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBom(BomSaveRequest request) {
        Bom existing = this.getById(request.getId());
        if (existing == null) {
            throw new BusinessException(404, "BOM不存在或已删除");
        }

        if (existing.getStatus() == 1) {
            throw new BusinessException(400, "生效中的 BOM 不允许修改");
        }

        Bom update = new Bom();
        update.setId(request.getId());
        update.setProductCode(request.getProductCode());
        update.setVersion(request.getVersion());
        this.updateById(update);

        // 删除旧明细
        bomItemMapper.delete(new LambdaQueryWrapper<BomItem>()
                .eq(BomItem::getBomId, request.getId()));

        // 新增新明细
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            List<BomItem> items = request.getItems().stream()
                    .map(item -> {
                        BomItem bomItem = new BomItem();
                        bomItem.setBomId(request.getId());
                        bomItem.setMaterialId(item.getMaterialId());
                        bomItem.setMaterialCode(item.getMaterialCode());
                        bomItem.setMaterialName(item.getMaterialName());
                        bomItem.setQuantity(item.getQuantity());
                        bomItem.setLossRate(item.getLossRate());
                        bomItem.setSortOrder(item.getSortOrder());
                        return bomItem;
                    })
                    .collect(Collectors.toList());

            bomItemMapper.insertBatch(items);
        }

        log.info("更新 BOM 成功: ID={}", request.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBom(Long id) {
        Bom existing = this.getById(id);
        if (existing == null) {
            throw new BusinessException(404, "BOM不存在或已删除");
        }

        if (existing.getStatus() == 1) {
            throw new BusinessException(400, "生效中的 BOM 不允许删除");
        }

        // 明细逻辑删除
        bomItemMapper.update(null, new LambdaUpdateWrapper<BomItem>()
                .eq(BomItem::getBomId, id)
                .set(BomItem::getIsDelete, 1));

        this.removeById(id);

        log.info("删除 BOM 成功: ID={}", id);
    }

    @Override
    public void changeStatus(Long id, Integer status) {
        Bom existing = this.getById(id);
        if (existing == null) {
            throw new BusinessException(404, "BOM不存在或已删除");
        }

        if (existing.getStatus() == 2) {
            throw new BusinessException(400, "已作废的 BOM 不能变更状态");
        }

        if (existing.getStatus() == 0 && status == 1) {
            List<BomItem> items = bomItemMapper.selectByBomId(id);
            if (items.isEmpty()) {
                throw new BusinessException(400, "BOM 明细不能为空");
            }
        }

        Bom update = new Bom();
        update.setId(id);
        update.setStatus(status);
        this.updateById(update);

        log.info("变更 BOM 状态成功: ID={}, 新状态={}", id, status);
    }
}

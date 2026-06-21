package com.lei.mes.service.impl.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mes.entity.common.ProductionLine;
import com.lei.mes.entity.common.Workstation;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.mapper.common.WorkstationMapper;
import com.lei.mes.mapper.common.ProductionLineMapper;
import com.lei.mes.request.common.ProductionLineSaveRequest;
import com.lei.mes.service.common.ProductionLineService;
import com.lei.mes.vo.common.ProductionLineVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ProductionLineServiceImpl extends ServiceImpl<ProductionLineMapper, ProductionLine>
        implements ProductionLineService {

    @Autowired
    private ProductionLineMapper productionLineMapper;

    @Autowired
    private WorkstationMapper workstationMapper;

    @Override
    public IPage<ProductionLineVO> getLinePage(int pageNum, int pageSize, 
                                                String lineName, Long workshopId) {
        Page<ProductionLine> page = new Page<>(pageNum, pageSize);
        return productionLineMapper.getLinePage(page, lineName, workshopId);
    }

    @Override
    public ProductionLineVO getLineDetail(Long id) {
        ProductionLineVO vo = productionLineMapper.getLineDetail(id);
        if (vo == null) {
            throw new BusinessException(404, "产线不存在或已删除");
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addLine(ProductionLineSaveRequest request) {
        // 检查编号唯一性
        long count = this.count(new LambdaQueryWrapper<ProductionLine>()
                .eq(ProductionLine::getLineCode, request.getLineCode())
                .eq(ProductionLine::getIsDelete, 0));
        if (count > 0) {
            throw new BusinessException(400, "产线编号已存在");
        }

        ProductionLine line = new ProductionLine();
        BeanUtils.copyProperties(request, line);
        line.setStatus(line.getStatus() != null ? line.getStatus() : 1);
        line.setIsDelete(0);
        this.save(line);

        log.info("新增产线成功: ID={}, 名称={}", line.getId(), line.getLineName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLine(ProductionLineSaveRequest request) {
        ProductionLine existing = this.getById(request.getId());
        if (existing == null) {
            throw new BusinessException(404, "产线不存在或已删除");
        }

        // 检查编号唯一性（排除自己）
        long count = this.count(new LambdaQueryWrapper<ProductionLine>()
                .eq(ProductionLine::getLineCode, request.getLineCode())
                .ne(ProductionLine::getId, request.getId())
                .eq(ProductionLine::getIsDelete, 0));
        if (count > 0) {
            throw new BusinessException(400, "产线编号已存在");
        }

        ProductionLine update = new ProductionLine();
        BeanUtils.copyProperties(request, update);
        update.setIsDelete(existing.getIsDelete());
        this.updateById(update);

        log.info("编辑产线成功: ID={}", request.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLine(Long id) {
        ProductionLine existing = this.getById(id);
        if (existing == null) {
            throw new BusinessException(404, "产线不存在或已删除");
        }

        // 检查是否有关联工位
        long stationCount = workstationMapper.selectCount(
                new LambdaQueryWrapper<Workstation>()
                        .eq(Workstation::getLineId, id)
                        .eq(Workstation::getIsDelete, 0));
        if (stationCount > 0) {
            throw new BusinessException(400, "该产线下存在工位，无法删除");
        }

        this.removeById(id);
        log.info("删除产线成功: ID={}", id);
    }

    @Override
    public List<ProductionLineVO> getAllLines() {
        return productionLineMapper.getAllLines();
    }
}

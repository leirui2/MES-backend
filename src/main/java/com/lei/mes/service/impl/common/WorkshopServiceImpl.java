package com.lei.mes.service.impl.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mes.entity.common.Workshop;
import com.lei.mes.entity.common.ProductionLine;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.mapper.common.ProductionLineMapper;
import com.lei.mes.mapper.common.WorkshopMapper;
import com.lei.mes.request.common.WorkshopSaveRequest;
import com.lei.mes.service.common.WorkshopService;
import com.lei.mes.vo.common.WorkshopVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class WorkshopServiceImpl extends ServiceImpl<WorkshopMapper, Workshop>
        implements WorkshopService {

    @Autowired
    private WorkshopMapper workshopMapper;

    @Autowired
    private ProductionLineMapper productionLineMapper;

    @Override
    public IPage<WorkshopVO> getWorkshopPage(int pageNum, int pageSize, String workshopName) {
        Page<Workshop> page = new Page<>(pageNum, pageSize);
        return workshopMapper.getWorkshopPage(page, workshopName);
    }

    @Override
    public WorkshopVO getWorkshopDetail(Long id) {
        WorkshopVO vo = workshopMapper.getWorkshopDetail(id);
        if (vo == null) {
            throw new BusinessException(404, "车间不存在或已删除");
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addWorkshop(WorkshopSaveRequest request) {
        // 检查编号唯一性
        long count = this.count(new LambdaQueryWrapper<Workshop>()
                .eq(Workshop::getWorkshopCode, request.getWorkshopCode())
                .eq(Workshop::getIsDelete, 0));
        if (count > 0) {
            throw new BusinessException(400, "车间编号已存在");
        }

        Workshop workshop = new Workshop();
        BeanUtils.copyProperties(request, workshop);
        workshop.setStatus(workshop.getStatus() != null ? workshop.getStatus() : 1);
        workshop.setIsDelete(0);
        this.save(workshop);

        log.info("新增车间成功: ID={}, 名称={}", workshop.getId(), workshop.getWorkshopName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWorkshop(WorkshopSaveRequest request) {
        Workshop existing = this.getById(request.getId());
        if (existing == null) {
            throw new BusinessException(404, "车间不存在或已删除");
        }

        // 检查编号唯一性（排除自己）
        long count = this.count(new LambdaQueryWrapper<Workshop>()
                .eq(Workshop::getWorkshopCode, request.getWorkshopCode())
                .ne(Workshop::getId, request.getId())
                .eq(Workshop::getIsDelete, 0));
        if (count > 0) {
            throw new BusinessException(400, "车间编号已存在");
        }

        Workshop update = new Workshop();
        BeanUtils.copyProperties(request, update);
        update.setIsDelete(existing.getIsDelete());
        this.updateById(update);

        log.info("编辑车间成功: ID={}", request.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWorkshop(Long id) {
        Workshop existing = this.getById(id);
        if (existing == null) {
            throw new BusinessException(404, "车间不存在或已删除");
        }

        // 检查是否有关联产线
        long lineCount = productionLineMapper.selectCount(
                new LambdaQueryWrapper<ProductionLine>()
                        .eq(ProductionLine::getWorkshopId, id)
                        .eq(ProductionLine::getIsDelete, 0));
        if (lineCount > 0) {
            throw new BusinessException(400, "该车间下存在产线，无法删除");
        }

        this.removeById(id);
        log.info("删除车间成功: ID={}", id);
    }

    @Override
    public List<WorkshopVO> getAllWorkshops() {
        return workshopMapper.getAllWorkshops();
    }
}

package com.lei.mes.service.impl.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mes.entity.common.ProcessRoute;
import com.lei.mes.entity.common.ProcessStep;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.mapper.common.ProcessRouteMapper;
import com.lei.mes.mapper.common.ProcessStepMapper;
import com.lei.mes.request.common.ProcessRouteSaveRequest;
import com.lei.mes.service.common.ProcessRouteService;
import com.lei.mes.vo.common.ProcessRouteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProcessRouteServiceImpl extends ServiceImpl<ProcessRouteMapper, ProcessRoute>
        implements ProcessRouteService {

    @Autowired
    private ProcessStepMapper processStepMapper;

    @Override
    public IPage<ProcessRouteVO> getRoutePage(int pageNum, int pageSize,
                                              String routeName, Long productId) {
        Page<ProcessRoute> page = new Page<>(pageNum, pageSize);
        return baseMapper.getRoutePage(page, routeName, productId);
    }

    @Override
    public ProcessRouteVO getRouteDetail(Long id) {
        ProcessRouteVO routeVO = baseMapper.getRouteDetail(id);
        if (routeVO == null) {
            throw new BusinessException(404, "工艺路线不存在或已删除");
        }
        return routeVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRoute(ProcessRouteSaveRequest request) {
        // 检查同产品下是否有同名工艺路线
        long count = this.count(new LambdaQueryWrapper<ProcessRoute>()
                .eq(ProcessRoute::getProductId, request.getProductId())
                .eq(ProcessRoute::getRouteName, request.getRouteName())
                .eq(ProcessRoute::getIsDelete, 0));
        if (count > 0) {
            throw new BusinessException(400, "该产品下已存在同名工艺路线");
        }

        ProcessRoute route = new ProcessRoute();
        route.setRouteName(request.getRouteName());
        route.setProductId(request.getProductId());
        route.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        this.save(route);

        // 保存工序
        if (request.getSteps() != null && !request.getSteps().isEmpty()) {
            List<ProcessStep> steps = request.getSteps().stream()
                    .map(step -> {
                        ProcessStep processStep = new ProcessStep();
                        processStep.setRouteId(route.getId());
                        processStep.setStepCode(step.getStepCode());
                        processStep.setStepName(step.getStepName());
                        processStep.setWorkstationId(step.getWorkstationId());
                        processStep.setSortOrder(step.getSortOrder() != null ? step.getSortOrder() : 0);
                        processStep.setStandardTime(step.getStandardTime());
                        processStep.setQcRequired(step.getQcRequired() != null ? step.getQcRequired() : 0);
                        return processStep;
                    })
                    .collect(Collectors.toList());

            processStepMapper.insertBatch(steps);
        }

        log.info("新增工艺路线成功: ID={}, 名称={}, 工序数={}",
                route.getId(), route.getRouteName(),
                request.getSteps() != null ? request.getSteps().size() : 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoute(ProcessRouteSaveRequest request) {
        ProcessRoute existing = this.getById(request.getId());
        if (existing == null) {
            throw new BusinessException(404, "工艺路线不存在或已删除");
        }

        // 检查同产品下是否有同名工艺路线（排除自己）
        long count = this.count(new LambdaQueryWrapper<ProcessRoute>()
                .eq(ProcessRoute::getProductId, request.getProductId())
                .eq(ProcessRoute::getRouteName, request.getRouteName())
                .ne(ProcessRoute::getId, request.getId())
                .eq(ProcessRoute::getIsDelete, 0));
        if (count > 0) {
            throw new BusinessException(400, "该产品下已存在同名工艺路线");
        }

        // 更新主表
        ProcessRoute update = new ProcessRoute();
        update.setId(request.getId());
        update.setRouteName(request.getRouteName());
        update.setProductId(request.getProductId());
        update.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        this.updateById(update);

        // 删除旧工序
        processStepMapper.delete(new LambdaQueryWrapper<ProcessStep>()
                .eq(ProcessStep::getRouteId, request.getId()));

        // 新增新工序
        if (request.getSteps() != null && !request.getSteps().isEmpty()) {
            List<ProcessStep> steps = request.getSteps().stream()
                    .map(step -> {
                        ProcessStep processStep = new ProcessStep();
                        processStep.setRouteId(request.getId());
                        processStep.setStepCode(step.getStepCode());
                        processStep.setStepName(step.getStepName());
                        processStep.setWorkstationId(step.getWorkstationId());
                        processStep.setSortOrder(step.getSortOrder() != null ? step.getSortOrder() : 0);
                        processStep.setStandardTime(step.getStandardTime());
                        processStep.setQcRequired(step.getQcRequired() != null ? step.getQcRequired() : 0);
                        return processStep;
                    })
                    .collect(Collectors.toList());

            processStepMapper.insertBatch(steps);
        }

        log.info("更新工艺路线成功: ID={}", request.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoute(Long id) {
        ProcessRoute existing = this.getById(id);
        if (existing == null) {
            throw new BusinessException(404, "工艺路线不存在或已删除");
        }

        // 逻辑删除工序
        processStepMapper.update(null, new LambdaUpdateWrapper<ProcessStep>()
                .eq(ProcessStep::getRouteId, id)
                .set(ProcessStep::getIsDelete, 1));

        // 逻辑删除主表
        this.removeById(id);

        log.info("删除工艺路线成功: ID={}", id);
    }

    @Override
    public ProcessRouteVO getRouteByProduct(Long productId) {
        ProcessRouteVO routeVO = baseMapper.getRouteByProduct(productId);
        if (routeVO == null) {
            throw new BusinessException(404, "该产品暂无工艺路线");
        }
        return routeVO;
    }
}

package com.lei.mes.controller.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lei.mes.common.Result;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.request.common.ProcessRouteSaveRequest;
import com.lei.mes.service.common.ProcessRouteService;
import com.lei.mes.vo.common.ProcessRouteVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 工艺路线管理控制器
 * @author lei
 */
@Slf4j
@RestController
@RequestMapping("/api/common/process-route")
public class ProcessRouteController {

    @Autowired
    private ProcessRouteService processRouteService;

    /**
     * 分页查询工艺路线列表
     */
    @GetMapping("/list")
    public Result<IPage<ProcessRouteVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String routeName,
            @RequestParam(required = false) Long productId) {

        IPage<ProcessRouteVO> page = processRouteService.getRoutePage(pageNum, pageSize, routeName, productId);
        return Result.success(page);
    }

    /**
     * 根据 工艺路线ID 查询工艺路线详情（含工序）
     */
    @GetMapping("/{id}")
    public Result<ProcessRouteVO> getById(@PathVariable Long id) {
        ProcessRouteVO routeVO = processRouteService.getRouteDetail(id);
        return Result.success(routeVO);
    }

    /**
     * 新增工艺路线
     */
    @PostMapping
    public Result<Void> add(@Valid @RequestBody ProcessRouteSaveRequest request) {
        try {
            processRouteService.addRoute(request);
            return Result.success("新增成功", null);
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 编辑工艺路线
     */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody ProcessRouteSaveRequest request) {
        try {
            processRouteService.updateRoute(request);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 删除工艺路线
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            processRouteService.deleteRoute(id);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 按产品ID查询工艺路线（包含工序）
     */
    @GetMapping("/by-product/{productId}")
    public Result<ProcessRouteVO> getByProduct(@PathVariable Long productId) {
        ProcessRouteVO routeVO = processRouteService.getRouteByProduct(productId);
        return Result.success(routeVO);
    }
}

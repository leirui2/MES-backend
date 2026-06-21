package com.lei.mes.controller.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lei.mes.common.Result;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.request.common.ProductionLineSaveRequest;
import com.lei.mes.service.common.ProductionLineService;
import com.lei.mes.vo.common.ProductionLineVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产线管理控制器
 * @author lei
 */
@Slf4j
@RestController
@RequestMapping("/api/common/production-line")
public class ProductionLineController {

    @Autowired
    private ProductionLineService productionLineService;

    /**
     * 分页查询产线列表
     */
    @GetMapping("/list")
    public Result<IPage<ProductionLineVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String lineName,
            @RequestParam(required = false) Long workshopId) {
        
        IPage<ProductionLineVO> page = productionLineService.getLinePage(pageNum, pageSize, lineName, workshopId);
        return Result.success(page);
    }

    /**
     * 根据ID查询产线详情（含工位）
     */
    @GetMapping("/{id}")
    public Result<ProductionLineVO> getById(@PathVariable Long id) {
        ProductionLineVO vo = productionLineService.getLineDetail(id);
        return Result.success(vo);
    }

    /**
     * 新增产线
     */
    @PostMapping
    public Result<Void> add(@Valid @RequestBody ProductionLineSaveRequest request) {
        try {
            productionLineService.addLine(request);
            return Result.success("新增成功", null);
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 编辑产线
     */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody ProductionLineSaveRequest request) {
        try {
            productionLineService.updateLine(request);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 删除产线
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            productionLineService.deleteLine(id);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 查询所有产线（下拉选择用）
     */
    @GetMapping("/options")
    public Result<List<ProductionLineVO>> options() {
        List<ProductionLineVO> list = productionLineService.getAllLines();
        return Result.success(list);
    }
}

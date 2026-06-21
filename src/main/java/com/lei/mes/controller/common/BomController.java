package com.lei.mes.controller.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lei.mes.common.Result;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.request.common.BomSaveRequest;
import com.lei.mes.service.common.BomService;
import com.lei.mes.vo.common.BomVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * BOM 控制器
 * @author lei
 */
@Slf4j
@RestController
@RequestMapping("/api/common/bom")
public class BomController {

    @Autowired
    private BomService bomService;

    /**
     * 分页查询 BOM 列表
     */
    @GetMapping("/list")
    public Result<IPage<BomVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String version,
            @RequestParam(required = false) Integer status) {

        return Result.success(bomService.getBomPage(pageNum, pageSize,
                productName, version, status));
    }

    /**
     * 根据 ID 查询 BOM 详情（含明细）
     */
    @GetMapping("/{id}")
    public Result<BomVO> getById(@PathVariable Long id) {
        BomVO bomVO = bomService.getBomDetail(id);
        return Result.success(bomVO);
    }

    /**
     * 新增 BOM
     */
    @PostMapping
    public Result<Void> add(@Valid @RequestBody BomSaveRequest request) {
        try {
            bomService.addBom(request);
            return Result.success("新增成功", null);
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 编辑 BOM
     */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody BomSaveRequest request) {
        try {
            bomService.updateBom(request);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 删除 BOM
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            bomService.deleteBom(id);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 变更 BOM 状态
     */
    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id,
                                     @RequestParam Integer status) {
        try {
            bomService.changeStatus(id, status);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }
}

package com.lei.mes.controller.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lei.mes.common.Result;
import com.lei.mes.entity.common.Material;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.request.common.MaterialSaveRequest;
import com.lei.mes.service.common.MaterialService;
import com.lei.mes.vo.common.MaterialVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 物料控制器
 * @author lei
 */
@Slf4j
@RestController
@RequestMapping("/api/common/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    /**
     * 分页查询物料列表
     */
    @GetMapping("/list")
    public Result<IPage<MaterialVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String materialName,
            @RequestParam(required = false) String category) {

        IPage<MaterialVO> page = materialService.getMaterialPage(pageNum, pageSize, materialName, category);
        return Result.success(page);
    }

    /**
     * 根据 ID 查询物料详情
     */
    @GetMapping("/{id}")
    public Result<Material> getById(@PathVariable Long id) {
        Material material = materialService.getById(id);
        if (material == null) {
            return Result.error(404, "物料不存在或已删除");
        }
        return Result.success(material);
    }

    /**
     * 新增物料
     */
    @PostMapping
    public Result<Void> add(@Valid @RequestBody MaterialSaveRequest request) {
        try {
            materialService.addMaterial(request);
            return Result.success("新增成功", null);
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 编辑物料
     */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody MaterialSaveRequest request) {
        try {
            materialService.updateMaterial(request);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 删除物料
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            materialService.deleteMaterial(id);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 切换产品状态（启用/禁用）
     */
    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            materialService.changeStatus(id, status);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }
}

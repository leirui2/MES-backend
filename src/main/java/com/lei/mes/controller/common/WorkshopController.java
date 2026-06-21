package com.lei.mes.controller.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lei.mes.common.Result;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.request.common.WorkshopSaveRequest;
import com.lei.mes.service.common.WorkshopService;
import com.lei.mes.vo.common.WorkshopVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车间管理控制器
 * @author lei
 */
@Slf4j
@RestController
@RequestMapping("/api/common/workshop")
public class WorkshopController {

    @Autowired
    private WorkshopService workshopService;

    /**
     * 分页查询车间列表
     */
    @GetMapping("/list")
    public Result<IPage<WorkshopVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String workshopName) {
        
        IPage<WorkshopVO> page = workshopService.getWorkshopPage(pageNum, pageSize, workshopName);
        return Result.success(page);
    }

    /**
     * 根据ID查询车间详情（含产线）
     */
    @GetMapping("/{id}")
    public Result<WorkshopVO> getById(@PathVariable Long id) {
        WorkshopVO vo = workshopService.getWorkshopDetail(id);
        return Result.success(vo);
    }

    /**
     * 新增车间
     */
    @PostMapping
    public Result<Void> add(@Valid @RequestBody WorkshopSaveRequest request) {
        try {
            workshopService.addWorkshop(request);
            return Result.success("新增成功", null);
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 编辑车间
     */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody WorkshopSaveRequest request) {
        try {
            workshopService.updateWorkshop(request);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 删除车间
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            workshopService.deleteWorkshop(id);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 查询所有车间（下拉选择用）
     */
    @GetMapping("/options")
    public Result<List<WorkshopVO>> options() {
        List<WorkshopVO> list = workshopService.getAllWorkshops();
        return Result.success(list);
    }
}

package com.lei.mes.controller.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lei.mes.common.Result;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.request.common.WorkstationSaveRequest;
import com.lei.mes.service.common.WorkstationService;
import com.lei.mes.vo.common.WorkstationVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工位管理控制器
 * @author lei
 */
@Slf4j
@RestController
@RequestMapping("/api/common/workstation")
public class WorkstationController {

    @Autowired
    private WorkstationService workstationService;

    /**
     * 分页查询工位列表
     */
    @GetMapping("/list")
    public Result<IPage<WorkstationVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String stationName,
            @RequestParam(required = false) Long lineId) {
        
        IPage<WorkstationVO> page = workstationService.getStationPage(pageNum, pageSize, stationName, lineId);
        return Result.success(page);
    }

    /**
     * 根据ID查询工位详情
     */
    @GetMapping("/{id}")
    public Result<WorkstationVO> getById(@PathVariable Long id) {
        WorkstationVO vo = workstationService.getStationDetail(id);
        return Result.success(vo);
    }

    /**
     * 新增工位
     */
    @PostMapping
    public Result<Void> add(@Valid @RequestBody WorkstationSaveRequest request) {
        try {
            workstationService.addStation(request);
            return Result.success("新增成功", null);
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 编辑工位
     */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody WorkstationSaveRequest request) {
        try {
            workstationService.updateStation(request);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 删除工位
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            workstationService.deleteStation(id);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 查询所有工位（下拉选择用）
     */
    @GetMapping("/options")
    public Result<List<WorkstationVO>> options() {
        List<WorkstationVO> list = workstationService.getAllStations();
        return Result.success(list);
    }
}

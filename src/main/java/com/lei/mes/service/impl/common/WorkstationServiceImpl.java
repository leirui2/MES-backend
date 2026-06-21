package com.lei.mes.service.impl.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mes.entity.common.Workstation;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.mapper.common.WorkstationMapper;
import com.lei.mes.request.common.WorkstationSaveRequest;
import com.lei.mes.service.common.WorkstationService;
import com.lei.mes.vo.common.WorkstationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class WorkstationServiceImpl extends ServiceImpl<WorkstationMapper, Workstation>
        implements WorkstationService {

    @Autowired
    private WorkstationMapper workstationMapper;

    @Override
    public IPage<WorkstationVO> getStationPage(int pageNum, int pageSize, 
                                                String stationName, Long lineId) {
        Page<Workstation> page = new Page<>(pageNum, pageSize);
        return workstationMapper.getStationPage(page, stationName, lineId);
    }

    @Override
    public WorkstationVO getStationDetail(Long id) {
        WorkstationVO vo = workstationMapper.getStationDetail(id);
        if (vo == null) {
            throw new BusinessException(404, "工位不存在或已删除");
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStation(WorkstationSaveRequest request) {
        // 检查编号唯一性
        long count = this.count(new LambdaQueryWrapper<Workstation>()
                .eq(Workstation::getStationCode, request.getStationCode())
                .eq(Workstation::getIsDelete, 0));
        if (count > 0) {
            throw new BusinessException(400, "工位编号已存在");
        }

        Workstation station = new Workstation();
        BeanUtils.copyProperties(request, station);
        station.setStatus(station.getStatus() != null ? station.getStatus() : 1);
        station.setIsDelete(0);
        this.save(station);

        log.info("新增工位成功: ID={}, 名称={}", station.getId(), station.getStationName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStation(WorkstationSaveRequest request) {
        Workstation existing = this.getById(request.getId());
        if (existing == null) {
            throw new BusinessException(404, "工位不存在或已删除");
        }

        // 检查编号唯一性（排除自己）
        long count = this.count(new LambdaQueryWrapper<Workstation>()
                .eq(Workstation::getStationCode, request.getStationCode())
                .ne(Workstation::getId, request.getId())
                .eq(Workstation::getIsDelete, 0));
        if (count > 0) {
            throw new BusinessException(400, "工位编号已存在");
        }

        Workstation update = new Workstation();
        BeanUtils.copyProperties(request, update);
        update.setIsDelete(existing.getIsDelete());
        this.updateById(update);

        log.info("编辑工位成功: ID={}", request.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStation(Long id) {
        Workstation existing = this.getById(id);
        if (existing == null) {
            throw new BusinessException(404, "工位不存在或已删除");
        }

        this.removeById(id);
        log.info("删除工位成功: ID={}", id);
    }

    @Override
    public List<WorkstationVO> getAllStations() {
        return workstationMapper.getAllStations();
    }
}

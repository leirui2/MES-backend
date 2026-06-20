package com.lei.mes.mapper.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lei.mes.entity.common.Material;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lei.mes.vo.common.MaterialVO;
import org.apache.ibatis.annotations.Param;

/**
* @author lei
* @description 针对表【material(物料表)】的数据库操作Mapper
* @createDate 2026-06-20 11:55:19
* @Entity com.lei.mes.entity.common.Material
*/
public interface MaterialMapper extends BaseMapper<Material> {


}





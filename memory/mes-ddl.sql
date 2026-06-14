-- =============================================
-- MES 系统 - 充电器制造场景
-- 技术栈：SpringBoot 3 + Vue3 + MySQL + MyBatis-Plus
-- 创建日期：2026-06-13
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS mes DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE mes;

-- =============================================
-- 第 1 周：基础建模
-- =============================================

-- 1. 产品表
CREATE TABLE `product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `product_code` VARCHAR(50) NOT NULL COMMENT '产品编号，唯一',
    `product_name` VARCHAR(100) NOT NULL COMMENT '产品名称',
    `specification` VARCHAR(200) COMMENT '规格型号',
    `unit` VARCHAR(10) COMMENT '计量单位（个/箱/件）',
    `category` VARCHAR(50) COMMENT '产品分类（快充/普通/无线）',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-停用 1-启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_code` (`product_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';

-- 2. 物料表
CREATE TABLE `material` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `material_code` VARCHAR(50) NOT NULL COMMENT '物料编号，唯一',
    `material_name` VARCHAR(100) NOT NULL COMMENT '物料名称',
    `specification` VARCHAR(200) COMMENT '规格型号',
    `unit` VARCHAR(10) COMMENT '计量单位',
    `category` VARCHAR(50) COMMENT '物料分类（芯片/外壳/线材/辅料）',
    `stock_qty` DECIMAL(10,2) DEFAULT 0.00 COMMENT '当前库存数量',
    `min_stock` DECIMAL(10,2) DEFAULT 0.00 COMMENT '安全库存下限',
    `unit_price` DECIMAL(10,2) DEFAULT 0.00 COMMENT '单价',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-停用 1-启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_material_code` (`material_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物料表';

-- 3. BOM 主表
CREATE TABLE `bom` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `product_id` BIGINT NOT NULL COMMENT '关联产品 ID',
    `product_code` VARCHAR(50) COMMENT '产品编号（冗余，方便查询）',
    `version` VARCHAR(20) NOT NULL COMMENT 'BOM 版本号',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-草稿 1-生效 2-作废',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='BOM 主表';

-- 4. BOM 明细表
CREATE TABLE `bom_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `bom_id` BIGINT NOT NULL COMMENT '关联 BOM 主表 ID',
    `material_id` BIGINT NOT NULL COMMENT '物料 ID',
    `material_code` VARCHAR(50) COMMENT '物料编号',
    `material_name` VARCHAR(100) COMMENT '物料名称',
    `quantity` DECIMAL(10,2) NOT NULL COMMENT '单件用量',
    `loss_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '损耗率（百分比）',
    `sort_order` INT DEFAULT 0 COMMENT '排序号',
    `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_bom_id` (`bom_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='BOM 明细表';

-- 5. 工艺路线表
CREATE TABLE `process_route` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `route_name` VARCHAR(100) NOT NULL COMMENT '工艺路线名称',
    `product_id` BIGINT NOT NULL COMMENT '关联产品 ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-停用 1-启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工艺路线表';

-- 6. 工序表
CREATE TABLE `process_step` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `route_id` BIGINT NOT NULL COMMENT '关联工艺路线 ID',
    `step_code` VARCHAR(50) NOT NULL COMMENT '工序编号',
    `step_name` VARCHAR(100) NOT NULL COMMENT '工序名称（如：焊接、组装、测试）',
    `workstation_id` BIGINT COMMENT '工位 ID',
    `sort_order` INT DEFAULT 0 COMMENT '工序顺序',
    `standard_time` DECIMAL(8,2) COMMENT '标准工时（秒）',
    `qc_required` TINYINT DEFAULT 0 COMMENT '是否需要质检：0-否 1-是',
    `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_route_id` (`route_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工序表';

-- 7. 车间表
CREATE TABLE `workshop` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `workshop_code` VARCHAR(50) NOT NULL COMMENT '车间编号',
    `workshop_name` VARCHAR(100) NOT NULL COMMENT '车间名称',
    `location` VARCHAR(200) COMMENT '位置描述',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-停用 1-启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_workshop_code` (`workshop_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='车间表';

-- 8. 产线表
CREATE TABLE `production_line` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `line_code` VARCHAR(50) NOT NULL COMMENT '产线编号',
    `line_name` VARCHAR(100) NOT NULL COMMENT '产线名称',
    `workshop_id` BIGINT NOT NULL COMMENT '所属车间 ID',
    `capacity_per_day` INT DEFAULT 0 COMMENT '日产能（件）',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-停用 1-启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_line_code` (`line_code`),
    KEY `idx_workshop_id` (`workshop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产线表';

-- 9. 工位表
CREATE TABLE `workstation` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `station_code` VARCHAR(50) NOT NULL COMMENT '工位编号',
    `station_name` VARCHAR(100) NOT NULL COMMENT '工位名称',
    `line_id` BIGINT NOT NULL COMMENT '所属产线 ID',
    `station_type` VARCHAR(50) COMMENT '工位类型（焊接/组装/测试/包装）',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-停用 1-启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_station_code` (`station_code`),
    KEY `idx_line_id` (`line_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工位表';

-- =============================================
-- 第 2 周：工单核心闭环
-- =============================================

-- 10. 工单主表
CREATE TABLE `work_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `order_no` VARCHAR(50) NOT NULL COMMENT '工单编号，唯一',
    `product_id` BIGINT NOT NULL COMMENT '产品 ID',
    `product_code` VARCHAR(50) COMMENT '产品编号',
    `product_name` VARCHAR(100) COMMENT '产品名称',
    `plan_qty` INT NOT NULL DEFAULT 0 COMMENT '计划数量',
    `done_qty` INT NOT NULL DEFAULT 0 COMMENT '已完成数量',
    `line_id` BIGINT COMMENT '所属产线 ID',
    `workshop_id` BIGINT COMMENT '所属车间 ID',
    `route_id` BIGINT COMMENT '工艺路线 ID',
    `plan_start` DATETIME COMMENT '计划开始时间',
    `plan_end` DATETIME COMMENT '计划结束时间',
    `actual_start` DATETIME COMMENT '实际开始时间',
    `actual_end` DATETIME COMMENT '实际结束时间',
    `status` VARCHAR(20) DEFAULT '待发布' COMMENT '状态：待发布/已下发/生产中/已完成/已取消',
    `operator_ids` VARCHAR(500) COMMENT '操作员 ID 列表（逗号分隔）',
    `remark` VARCHAR(500) COMMENT '备注',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_line_id` (`line_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单主表';

-- 11. 工单报工记录表
CREATE TABLE `work_report` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `order_id` BIGINT NOT NULL COMMENT '工单 ID',
    `order_no` VARCHAR(50) COMMENT '工单编号',
    `step_id` BIGINT COMMENT '工序 ID',
    `step_name` VARCHAR(100) COMMENT '工序名称',
    `station_id` BIGINT COMMENT '工位 ID',
    `operator_id` BIGINT COMMENT '操作员 ID',
    `operator_name` VARCHAR(50) COMMENT '操作员姓名',
    `report_qty` INT NOT NULL DEFAULT 0 COMMENT '报工数量',
    `good_qty` INT NOT NULL DEFAULT 0 COMMENT '合格数量',
    `defect_qty` INT NOT NULL DEFAULT 0 COMMENT '不良数量',
    `defect_reason` VARCHAR(200) COMMENT '不良原因',
    `report_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '报工时间',
    `remark` VARCHAR(500) COMMENT '备注',
    `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_report_time` (`report_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单报工记录表';

-- =============================================
-- 第 3 周：物料管理
-- =============================================

-- 12. 领料单表
CREATE TABLE `material_issue` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `issue_no` VARCHAR(50) NOT NULL COMMENT '领料单号，唯一',
    `order_id` BIGINT NOT NULL COMMENT '关联工单 ID',
    `order_no` VARCHAR(50) COMMENT '工单编号',
    `applicant_id` BIGINT COMMENT '申请人 ID',
    `applicant_name` VARCHAR(50) COMMENT '申请人姓名',
    `warehouse_id` BIGINT COMMENT '仓库 ID',
    `issue_date` DATE COMMENT '领料日期',
    `status` VARCHAR(20) DEFAULT '待审批' COMMENT '状态：待审批/已审批/已发料/已退回',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_issue_no` (`issue_no`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='领料单表';

-- 13. 领料明细表
CREATE TABLE `material_issue_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `issue_id` BIGINT NOT NULL COMMENT '领料单 ID',
    `material_id` BIGINT NOT NULL COMMENT '物料 ID',
    `material_code` VARCHAR(50) COMMENT '物料编号',
    `material_name` VARCHAR(100) COMMENT '物料名称',
    `apply_qty` DECIMAL(10,2) NOT NULL COMMENT '申请数量',
    `actual_qty` DECIMAL(10,2) NOT NULL COMMENT '实发数量',
    `unit` VARCHAR(10) COMMENT '单位',
        `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_issue_id` (`issue_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='领料明细表';

-- 14. 退料单表
CREATE TABLE `material_return` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `return_no` VARCHAR(50) NOT NULL COMMENT '退料单号，唯一',
    `order_id` BIGINT NOT NULL COMMENT '关联工单 ID',
    `material_id` BIGINT NOT NULL COMMENT '物料 ID',
    `material_code` VARCHAR(50) COMMENT '物料编号',
    `return_qty` DECIMAL(10,2) NOT NULL COMMENT '退料数量',
    `return_reason` VARCHAR(200) COMMENT '退料原因',
    `return_date` DATE COMMENT '退料日期',
        `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_return_no` (`return_no`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退料单表';

-- =============================================
-- 第 4 周：质量管理
-- =============================================

-- 15. 质检标准表
CREATE TABLE `qc_standard` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `standard_code` VARCHAR(50) NOT NULL COMMENT '标准编号',
    `standard_name` VARCHAR(100) NOT NULL COMMENT '标准名称',
    `product_id` BIGINT COMMENT '适用产品 ID',
    `step_id` BIGINT COMMENT '适用工序 ID',
    `check_item` VARCHAR(200) NOT NULL COMMENT '检验项目（如：电压、外观、尺寸）',
    `upper_limit` DECIMAL(10,2) COMMENT '上限值',
    `lower_limit` DECIMAL(10,2) COMMENT '下限值',
    `standard_desc` VARCHAR(500) COMMENT '判定标准描述',
        `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_standard_code` (`standard_code`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_step_id` (`step_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='质检标准表';

-- 16. 质检记录表
CREATE TABLE `qc_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `record_no` VARCHAR(50) NOT NULL COMMENT '质检单号，唯一',
    `order_id` BIGINT NOT NULL COMMENT '工单 ID',
    `order_no` VARCHAR(50) COMMENT '工单编号',
    `step_id` BIGINT COMMENT '工序 ID',
    `batch_no` VARCHAR(50) COMMENT '批次号',
    `check_qty` INT NOT NULL DEFAULT 0 COMMENT '检验数量',
    `pass_qty` INT NOT NULL DEFAULT 0 COMMENT '合格数量',
    `fail_qty` INT NOT NULL DEFAULT 0 COMMENT '不合格数量',
    `result` VARCHAR(20) COMMENT '结果：PASS/FAIL',
    `inspector_id` BIGINT COMMENT '检验员 ID',
    `inspector_name` VARCHAR(50) COMMENT '检验员姓名',
    `check_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '检验时间',
    `remark` VARCHAR(500) COMMENT '备注',
        `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_record_no` (`record_no`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_check_time` (`check_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='质检记录表';

-- 17. 质检明细表
CREATE TABLE `qc_record_detail` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `record_id` BIGINT NOT NULL COMMENT '质检记录 ID',
    `standard_id` BIGINT COMMENT '质检标准 ID',
    `check_item` VARCHAR(200) COMMENT '检验项目',
    `actual_value` VARCHAR(200) COMMENT '实测值',
    `result` VARCHAR(10) COMMENT '结果：PASS/FAIL',
    `remark` VARCHAR(200) COMMENT '备注',
        `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_record_id` (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='质检明细表';

-- 18. 追溯表
CREATE TABLE `traceability` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `serial_no` VARCHAR(100) NOT NULL COMMENT '序列号（SN 码）',
    `product_id` BIGINT NOT NULL COMMENT '产品 ID',
    `order_id` BIGINT NOT NULL COMMENT '工单 ID',
    `batch_no` VARCHAR(50) COMMENT '批次号',
    `material_ids` VARCHAR(500) COMMENT '所用物料 ID 列表',
    `operator_ids` VARCHAR(500) COMMENT '操作员 ID 列表',
    `workstation_ids` VARCHAR(500) COMMENT '经过工位 ID 列表',
    `qc_result` VARCHAR(20) COMMENT '最终质检结果',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_serial_no` (`serial_no`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='追溯表';

-- =============================================
-- 第 5 周：设备与看板
-- =============================================

-- 19. 设备表
CREATE TABLE `equipment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `equip_code` VARCHAR(50) NOT NULL COMMENT '设备编号',
    `equip_name` VARCHAR(100) NOT NULL COMMENT '设备名称',
    `equip_type` VARCHAR(50) COMMENT '设备类型（注塑机/贴片机/测试台）',
    `workshop_id` BIGINT COMMENT '所属车间 ID',
    `line_id` BIGINT COMMENT '所属产线 ID',
    `status` VARCHAR(20) DEFAULT '运行' COMMENT '状态：运行/停机/维修/报废',
    `last_maintain_date` DATE COMMENT '上次保养日期',
    `next_maintain_date` DATE COMMENT '下次保养日期',
    `manufacturer` VARCHAR(100) COMMENT '制造商',
    `purchase_date` DATE COMMENT '购买日期',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_equip_code` (`equip_code`),
    KEY `idx_workshop_id` (`workshop_id`),
    KEY `idx_line_id` (`line_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备表';

-- 20. 设备保养记录表
CREATE TABLE `maintain_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `equip_id` BIGINT NOT NULL COMMENT '设备 ID',
    `maintain_type` VARCHAR(50) COMMENT '保养类型（日常/月度/年度/维修）',
    `maintain_content` VARCHAR(500) COMMENT '保养内容',
    `maintainer_id` BIGINT COMMENT '保养人 ID',
    `maintain_date` DATE COMMENT '保养日期',
    `next_date` DATE COMMENT '下次保养日期',
    `remark` VARCHAR(500) COMMENT '备注',
        `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_equip_id` (`equip_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备保养记录表';

-- 21. 设备故障记录表
CREATE TABLE `equipment_fault` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `equip_id` BIGINT NOT NULL COMMENT '设备 ID',
    `fault_desc` VARCHAR(500) COMMENT '故障描述',
    `fault_level` TINYINT COMMENT '故障等级：1-轻微 2-一般 3-严重',
    `report_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '报修时间',
    `repair_time` DATETIME COMMENT '修复时间',
    `repair_person` VARCHAR(50) COMMENT '维修人员',
    `status` VARCHAR(20) DEFAULT '待修' COMMENT '状态：待修/维修中/已修复',
        `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_equip_id` (`equip_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备故障记录表';

-- =============================================
-- 第 6 周：完善与扩展
-- =============================================

-- 22. 班次表
CREATE TABLE `shift` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `shift_name` VARCHAR(50) NOT NULL COMMENT '班次名称（白班/夜班）',
    `start_time` TIME COMMENT '开始时间',
    `end_time` TIME COMMENT '结束时间',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-停用 1-启用',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班次表';

-- 23. 员工表
CREATE TABLE `employee` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `emp_no` VARCHAR(50) NOT NULL COMMENT '工号',
    `emp_name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `phone` VARCHAR(20) COMMENT '手机号',
    `department` VARCHAR(50) COMMENT '部门',
    `position` VARCHAR(50) COMMENT '岗位',
    `shift_id` BIGINT COMMENT '所属班次',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-离职 1-在职',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_emp_no` (`emp_no`),
    KEY `idx_shift_id` (`shift_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- 24. 报表统计中间表
CREATE TABLE `report_daily` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `stat_date` DATE NOT NULL COMMENT '统计日期',
    `workshop_id` BIGINT COMMENT '车间 ID',
    `line_id` BIGINT COMMENT '产线 ID',
    `plan_qty` INT DEFAULT 0 COMMENT '计划产量',
    `done_qty` INT DEFAULT 0 COMMENT '完成产量',
    `good_qty` INT DEFAULT 0 COMMENT '合格产量',
    `defect_qty` INT DEFAULT 0 COMMENT '不良产量',
    `yield_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '良品率（%）',
    `oee` DECIMAL(5,2) DEFAULT 0.00 COMMENT 'OEE（设备综合效率）',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_date_workshop_line` (`stat_date`, `workshop_id`, `line_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表统计中间表';

-- =============================================
-- 权限管理模块（用户 + RBAC 角色权限）
-- =============================================

-- 25. 用户表
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `username` VARCHAR(50) NOT NULL COMMENT '登录账号',
    `password` VARCHAR(200) NOT NULL COMMENT '密码（BCrypt 加密）',
    `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `avatar` VARCHAR(500) COMMENT '头像 URL',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
    `dept_id` BIGINT COMMENT '部门 ID',
    `last_login_time` DATETIME COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) COMMENT '最后登录 IP',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 26. 角色表
CREATE TABLE `sys_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码（ADMIN/OPERATOR/QC等）',
    `description` VARCHAR(200) COMMENT '描述',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 27. 用户-角色关联表
CREATE TABLE `sys_user_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `user_id` BIGINT NOT NULL COMMENT '用户 ID',
    `role_id` BIGINT NOT NULL COMMENT '角色 ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-角色关联表';

-- 28. 菜单表（权限标识集中在这里）
CREATE TABLE `sys_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父菜单 ID（0=顶级）',
    `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
    `menu_code` VARCHAR(100) COMMENT '菜单编码',
    `menu_type` TINYINT NOT NULL COMMENT '类型：1-目录 2-菜单 3-按钮',
    `url` VARCHAR(200) COMMENT '路由地址',
    `icon` VARCHAR(100) COMMENT '图标',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `perms` VARCHAR(200) COMMENT '权限标识（如：work-order:dispatch）',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-隐藏 1-显示',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        `is_delete` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单（权限）表';

-- 29. 角色-菜单关联表
CREATE TABLE `sys_role_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `role_id` BIGINT NOT NULL COMMENT '角色 ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单 ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`),
    KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-菜单关联表';

-- =============================================
-- 初始化数据（超级管理员 + 默认角色 + 菜单）
-- =============================================

-- 初始化超级管理员账号（密码：admin123，BCrypt 加密后）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '超级管理员', 1);

-- 初始化默认角色
INSERT INTO `sys_role` (`role_name`, `role_code`, `description`, `status`) VALUES
('超级管理员', 'ADMIN', '拥有所有权限', 1),
('生产经理', 'MANAGER', '工单管理、报表查看、人员管理', 1),
('班组长', 'LEADER', '工单下发、报工审核、物料审批', 1),
('操作工', 'OPERATOR', '报工、领料申请、查看个人任务', 1),
('质检员', 'QC', '质检标准、质检记录、追溯查询', 1),
('仓管员', 'WAREHOUSE', '物料管理、领料/退料审批', 1);

-- 初始化超级管理员角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- 初始化菜单（示例）
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_code`, `menu_type`, `url`, `icon`, `sort_order`, `perms`, `status`) VALUES
-- 一级菜单
(0, '系统管理', 'system', 1, '', 'settings', 1, NULL, 1),
(0, '产品管理', 'product', 2, '/product', 'box', 2, 'product:list', 1),
(0, '物料管理', 'material', 2, '/material', 'package', 3, 'material:list', 1),
(0, '工单管理', 'workorder', 2, '/workorder', 'list', 4, 'work-order:list', 1),
(0, '生产报工', 'report', 2, '/report', 'edit', 5, 'work-report:list', 1),
(0, '质量管理', 'quality', 2, '/quality', 'check', 6, 'qc:list', 1),
(0, '设备管理', 'equipment', 2, '/equipment', 'cog', 7, 'equipment:list', 1);

-- 子菜单（系统管理）
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_code`, `menu_type`, `url`, `icon`, `sort_order`, `perms`, `status`) VALUES
(1, '用户管理', 'system-user', 2, '/system/user', 'user', 1, 'system:user:list', 1),
(1, '角色管理', 'system-role', 2, '/system/role', 'users', 2, 'system:role:list', 1),
(1, '菜单管理', 'system-menu', 2, '/system/menu', 'menu', 3, 'system:menu:list', 1);

-- 按钮权限（产品管理）
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_code`, `menu_type`, `url`, `icon`, `sort_order`, `perms`, `status`) VALUES
(2, '新增', NULL, 3, NULL, NULL, 1, 'product:add', 1),
(2, '编辑', NULL, 3, NULL, NULL, 2, 'product:edit', 1),
(2, '删除', NULL, 3, NULL, NULL, 3, 'product:delete', 1);

-- 将超级管理员关联所有菜单
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, id FROM `sys_menu`;

-- 将班组长关联生产相关菜单
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(3, 2),  -- 产品管理
(3, 4),  -- 工单管理
(3, 5);  -- 生产报工

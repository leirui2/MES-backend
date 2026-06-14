# MES 学习路线图（6 周）

> **项目背景：** 充电器制造 MES 系统
> **技术栈：** SpringBoot 3 + Vue3 + MySQL + MyBatis-Plus
> **场景：** 以"充电器"生产为主线，贯穿工单→领料→生产→质检→入库全流程

---

## 第 1 周：基础建模

### 1.1 产品建模

#### 产品表 `product`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `product_code` | VARCHAR(50) | 产品编号，唯一 |
| `product_name` | VARCHAR(100) | 产品名称 |
| `specification` | VARCHAR(200) | 规格型号 |
| `unit` | VARCHAR(10) | 计量单位（个/箱/件） |
| `category` | VARCHAR(50) | 产品分类（快充/普通/无线） |
| `status` | TINYINT | 状态：0-停用 1-启用 |
| `created_at` | DATETIME | 创建时间 |
| `updated_at` | DATETIME | 更新时间 |

#### 物料表 `material`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `material_code` | VARCHAR(50) | 物料编号，唯一 |
| `material_name` | VARCHAR(100) | 物料名称 |
| `specification` | VARCHAR(200) | 规格型号 |
| `unit` | VARCHAR(10) | 计量单位 |
| `category` | VARCHAR(50) | 物料分类（芯片/外壳/线材/辅料） |
| `stock_qty` | DECIMAL(10,2) | 当前库存数量 |
| `min_stock` | DECIMAL(10,2) | 安全库存下限 |
| `unit_price` | DECIMAL(10,2) | 单价 |
| `status` | TINYINT | 状态：0-停用 1-启用 |
| `created_at` | DATETIME | 创建时间 |
| `updated_at` | DATETIME | 更新时间 |

### 1.2 BOM（物料清单）

#### BOM 主表 `bom`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `product_id` | BIGINT | 关联产品 ID |
| `product_code` | VARCHAR(50) | 产品编号（冗余，方便查询） |
| `version` | VARCHAR(20) | BOM 版本号 |
| `status` | TINYINT | 状态：0-草稿 1-生效 2-作废 |
| `created_at` | DATETIME | 创建时间 |
| `updated_at` | DATETIME | 更新时间 |

#### BOM 明细表 `bom_item`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `bom_id` | BIGINT | 关联 BOM 主表 ID |
| `material_id` | BIGINT | 物料 ID |
| `material_code` | VARCHAR(50) | 物料编号 |
| `material_name` | VARCHAR(100) | 物料名称 |
| `quantity` | DECIMAL(10,2) | 单件用量 |
| `loss_rate` | DECIMAL(5,2) | 损耗率（百分比） |
| `sort_order` | INT | 排序号 |

### 1.3 工艺路线

#### 工艺路线表 `process_route`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `route_name` | VARCHAR(100) | 工艺路线名称 |
| `product_id` | BIGINT | 关联产品 ID |
| `status` | TINYINT | 状态：0-停用 1-启用 |
| `created_at` | DATETIME | 创建时间 |
| `updated_at` | DATETIME | 更新时间 |

#### 工序表 `process_step`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `route_id` | BIGINT | 关联工艺路线 ID |
| `step_code` | VARCHAR(50) | 工序编号 |
| `step_name` | VARCHAR(100) | 工序名称（如：焊接、组装、测试） |
| `workstation_id` | BIGINT | 工位 ID |
| `sort_order` | INT | 工序顺序 |
| `standard_time` | DECIMAL(8,2) | 标准工时（秒） |
| `qc_required` | TINYINT | 是否需要质检：0-否 1-是 |

### 1.4 车间/产线/工位

#### 车间表 `workshop`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `workshop_code` | VARCHAR(50) | 车间编号 |
| `workshop_name` | VARCHAR(100) | 车间名称 |
| `location` | VARCHAR(200) | 位置描述 |
| `status` | TINYINT | 状态：0-停用 1-启用 |
| `created_at` | DATETIME | 创建时间 |
| `updated_at` | DATETIME | 更新时间 |

#### 产线表 `production_line`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `line_code` | VARCHAR(50) | 产线编号 |
| `line_name` | VARCHAR(100) | 产线名称 |
| `workshop_id` | BIGINT | 所属车间 ID |
| `capacity_per_day` | INT | 日产能（件） |
| `status` | TINYINT | 状态：0-停用 1-启用 |
| `created_at` | DATETIME | 创建时间 |
| `updated_at` | DATETIME | 更新时间 |

#### 工位表 `workstation`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `station_code` | VARCHAR(50) | 工位编号 |
| `station_name` | VARCHAR(100) | 工位名称 |
| `line_id` | BIGINT | 所属产线 ID |
| `station_type` | VARCHAR(50) | 工位类型（焊接/组装/测试/包装） |
| `status` | TINYINT | 状态：0-停用 1-启用 |
| `created_at` | DATETIME | 创建时间 |
| `updated_at` | DATETIME | 更新时间 |

---

## 第 2 周：工单核心闭环

### 2.1 工单主表 `work_order`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `order_no` | VARCHAR(50) | 工单编号，唯一 |
| `product_id` | BIGINT | 产品 ID |
| `product_code` | VARCHAR(50) | 产品编号 |
| `product_name` | VARCHAR(100) | 产品名称 |
| `plan_qty` | INT | 计划数量 |
| `done_qty` | INT | 已完成数量 |
| `line_id` | BIGINT | 所属产线 ID |
| `workshop_id` | BIGINT | 所属车间 ID |
| `route_id` | BIGINT | 工艺路线 ID |
| `plan_start` | DATETIME | 计划开始时间 |
| `plan_end` | DATETIME | 计划结束时间 |
| `actual_start` | DATETIME | 实际开始时间 |
| `actual_end` | DATETIME | 实际结束时间 |
| `status` | VARCHAR(20) | 状态：待发布/已下发/生产中/已完成/已取消 |
| `operator_ids` | VARCHAR(500) | 操作员 ID 列表（逗号分隔） |
| `remark` | VARCHAR(500) | 备注 |
| `created_at` | DATETIME | 创建时间 |
| `updated_at` | DATETIME | 更新时间 |

### 2.2 工单报工记录表 `work_report`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `order_id` | BIGINT | 工单 ID |
| `order_no` | VARCHAR(50) | 工单编号 |
| `step_id` | BIGINT | 工序 ID |
| `step_name` | VARCHAR(100) | 工序名称 |
| `station_id` | BIGINT | 工位 ID |
| `operator_id` | BIGINT | 操作员 ID |
| `operator_name` | VARCHAR(50) | 操作员姓名 |
| `report_qty` | INT | 报工数量 |
| `good_qty` | INT | 合格数量 |
| `defect_qty` | INT | 不良数量 |
| `defect_reason` | VARCHAR(200) | 不良原因 |
| `report_time` | DATETIME | 报工时间 |
| `remark` | VARCHAR(500) | 备注 |

---

## 第 3 周：物料管理

### 3.1 领料单表 `material_issue`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `issue_no` | VARCHAR(50) | 领料单号，唯一 |
| `order_id` | BIGINT | 关联工单 ID |
| `order_no` | VARCHAR(50) | 工单编号 |
| `applicant_id` | BIGINT | 申请人 ID |
| `applicant_name` | VARCHAR(50) | 申请人姓名 |
| `warehouse_id` | BIGINT | 仓库 ID |
| `issue_date` | DATE | 领料日期 |
| `status` | VARCHAR(20) | 状态：待审批/已审批/已发料/已退回 |
| `created_at` | DATETIME | 创建时间 |
| `updated_at` | DATETIME | 更新时间 |

### 3.2 领料明细表 `material_issue_item`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `issue_id` | BIGINT | 领料单 ID |
| `material_id` | BIGINT | 物料 ID |
| `material_code` | VARCHAR(50) | 物料编号 |
| `material_name` | VARCHAR(100) | 物料名称 |
| `apply_qty` | DECIMAL(10,2) | 申请数量 |
| `actual_qty` | DECIMAL(10,2) | 实发数量 |
| `unit` | VARCHAR(10) | 单位 |

### 3.3 退料单表 `material_return`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `return_no` | VARCHAR(50) | 退料单号，唯一 |
| `order_id` | BIGINT | 关联工单 ID |
| `material_id` | BIGINT | 物料 ID |
| `material_code` | VARCHAR(50) | 物料编号 |
| `return_qty` | DECIMAL(10,2) | 退料数量 |
| `return_reason` | VARCHAR(200) | 退料原因 |
| `return_date` | DATE | 退料日期 |
| `created_at` | DATETIME | 创建时间 |

---

## 第 4 周：质量管理

### 4.1 质检标准表 `qc_standard`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `standard_code` | VARCHAR(50) | 标准编号 |
| `standard_name` | VARCHAR(100) | 标准名称 |
| `product_id` | BIGINT | 适用产品 ID |
| `step_id` | BIGINT | 适用工序 ID |
| `check_item` | VARCHAR(200) | 检验项目（如：电压、外观、尺寸） |
| `upper_limit` | DECIMAL(10,2) | 上限值 |
| `lower_limit` | DECIMAL(10,2) | 下限值 |
| `standard_desc` | VARCHAR(500) | 判定标准描述 |
| `created_at` | DATETIME | 创建时间 |

### 4.2 质检记录表 `qc_record`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `record_no` | VARCHAR(50) | 质检单号，唯一 |
| `order_id` | BIGINT | 工单 ID |
| `order_no` | VARCHAR(50) | 工单编号 |
| `step_id` | BIGINT | 工序 ID |
| `batch_no` | VARCHAR(50) | 批次号 |
| `check_qty` | INT | 检验数量 |
| `pass_qty` | INT | 合格数量 |
| `fail_qty` | INT | 不合格数量 |
| `result` | VARCHAR(20) | 结果：PASS/FAIL |
| `inspector_id` | BIGINT | 检验员 ID |
| `inspector_name` | VARCHAR(50) | 检验员姓名 |
| `check_time` | DATETIME | 检验时间 |
| `remark` | VARCHAR(500) | 备注 |

### 4.3 质检明细表 `qc_record_detail`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `record_id` | BIGINT | 质检记录 ID |
| `standard_id` | BIGINT | 质检标准 ID |
| `check_item` | VARCHAR(200) | 检验项目 |
| `actual_value` | VARCHAR(200) | 实测值 |
| `result` | VARCHAR(10) | 结果：PASS/FAIL |
| `remark` | VARCHAR(200) | 备注 |

### 4.4 追溯表 `traceability`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `serial_no` | VARCHAR(100) | 序列号（SN 码） |
| `product_id` | BIGINT | 产品 ID |
| `order_id` | BIGINT | 工单 ID |
| `batch_no` | VARCHAR(50) | 批次号 |
| `material_ids` | VARCHAR(500) | 所用物料 ID 列表 |
| `operator_ids` | VARCHAR(500) | 操作员 ID 列表 |
| `workstation_ids` | VARCHAR(500) | 经过工位 ID 列表 |
| `qc_result` | VARCHAR(20) | 最终质检结果 |
| `created_at` | DATETIME | 创建时间 |

---

## 第 5 周：设备与看板

### 5.1 设备表 `equipment`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `equip_code` | VARCHAR(50) | 设备编号 |
| `equip_name` | VARCHAR(100) | 设备名称 |
| `equip_type` | VARCHAR(50) | 设备类型（注塑机/贴片机/测试台） |
| `workshop_id` | BIGINT | 所属车间 ID |
| `line_id` | BIGINT | 所属产线 ID |
| `status` | VARCHAR(20) | 状态：运行/停机/维修/报废 |
| `last_maintain_date` | DATE | 上次保养日期 |
| `next_maintain_date` | DATE | 下次保养日期 |
| `manufacturer` | VARCHAR(100) | 制造商 |
| `purchase_date` | DATE | 购买日期 |
| `created_at` | DATETIME | 创建时间 |
| `updated_at` | DATETIME | 更新时间 |

### 5.2 设备保养记录表 `maintain_record`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `equip_id` | BIGINT | 设备 ID |
| `maintain_type` | VARCHAR(50) | 保养类型（日常/月度/年度/维修） |
| `maintain_content` | VARCHAR(500) | 保养内容 |
| `maintainer_id` | BIGINT | 保养人 ID |
| `maintain_date` | DATE | 保养日期 |
| `next_date` | DATE | 下次保养日期 |
| `remark` | VARCHAR(500) | 备注 |

### 5.3 设备故障记录表 `equipment_fault`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `equip_id` | BIGINT | 设备 ID |
| `fault_desc` | VARCHAR(500) | 故障描述 |
| `fault_level` | TINYINT | 故障等级：1-轻微 2-一般 3-严重 |
| `report_time` | DATETIME | 报修时间 |
| `repair_time` | DATETIME | 修复时间 |
| `repair_person` | VARCHAR(50) | 维修人员 |
| `status` | VARCHAR(20) | 状态：待修/维修中/已修复 |
| `created_at` | DATETIME | 创建时间 |

---

## 第 6 周：完善与扩展

### 6.1 班次表 `shift`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `shift_name` | VARCHAR(50) | 班次名称（白班/夜班） |
| `start_time` | TIME | 开始时间 |
| `end_time` | TIME | 结束时间 |
| `status` | TINYINT | 状态：0-停用 1-启用 |

### 6.2 员工表 `employee`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `emp_no` | VARCHAR(50) | 工号 |
| `emp_name` | VARCHAR(50) | 姓名 |
| `phone` | VARCHAR(20) | 手机号 |
| `department` | VARCHAR(50) | 部门 |
| `position` | VARCHAR(50) | 岗位 |
| `shift_id` | BIGINT | 所属班次 |
| `status` | TINYINT | 状态：0-离职 1-在职 |
| `created_at` | DATETIME | 创建时间 |
| `updated_at` | DATETIME | 更新时间 |

### 6.3 报表统计中间表 `report_daily`
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `stat_date` | DATE | 统计日期 |
| `workshop_id` | BIGINT | 车间 ID |
| `line_id` | BIGINT | 产线 ID |
| `plan_qty` | INT | 计划产量 |
| `done_qty` | INT | 完成产量 |
| `good_qty` | INT | 合格产量 |
| `defect_qty` | INT | 不良产量 |
| `yield_rate` | DECIMAL(5,2) | 良品率（%） |
| `oee` | DECIMAL(5,2) | OEE（设备综合效率） |
| `created_at` | DATETIME | 创建时间 |

# MES 系统 - 开发任务清单

> **项目名称：** 充电器制造 MES 系统
> **技术栈：** SpringBoot 3 + Vue3 + MySQL 8 + MyBatis-Plus + JJWT
> **创建日期：** 2026-06-13
> **版本：** v1.0
> **总计：** 6 大模块，42 个任务，预计 6 周

---

## 📋 任务优先级说明

| 标记 | 含义 |
|------|------|
| 🔴 **P0** | 核心路径，必须优先完成 |
| 🟡 **P1** | 重要功能，P0 完成后进行 |
| 🟢 **P2** | 增强功能，最后完善 |

---

## 第一阶段：项目搭建（第 1 天）

### Task 1.1 - 环境初始化 🔴
- [x] 创建 SpringBoot 项目（E:\JAVA\workspace\MES-lei\Mes-backend）
- [x] 配置 pom.xml：SpringBoot 3 + MyBatis-Plus + JJWT + MySQL + Validation
- [x] 配置 application.yml：数据库连接、MyBatis-Plus 扫描路径
- [x] 创建包结构：controller / service / mapper / entity / config / exception / common

### Task 1.2 - 基础设施配置 🔴
- [x] 全局异常处理器 `GlobalExceptionHandler`
- [x] 统一响应类 `Result` + 错误码枚举 `ErrorCode`
- [x] 跨域配置 `CorsConfig`
- [x] Spring Security 开发模式配置（放行所有接口）
- [x] MyBatis-Plus 配置类（分页插件、自动填充）

### Task 1.3 - 数据库初始化 🔴
- [x] 创建 mes 数据库
- [x] 执行 mes-ddl.sql 建表（29 张表）
- [x] 插入初始化数据（管理员账号、默认角色、菜单）

---

## 第二阶段：权限管理模块（第 1 周）

### Task 2.1 - 用户管理 🔴
- [x] 创建 `SysUser` 实体类（Lombok + MyBatis-Plus 注解）
- [x] 创建 `SysUserMapper` 接口
- [x] 创建 `SysUserService` + `SysUserServiceImpl`
- [x] 创建 `SysUserController`（CRUD 接口）
- [x] 实现用户分页查询、新增、编辑、删除、状态切换

### Task 2.2 - JWT 工具类 🔴
- [x] 创建 `JwtUtils` 工具类（生成 Token、解析 Token、验证 Token）
- [x] 实现 Token 过期时间配置
- [x] 实现从 Token 提取用户名和用户 ID

### Task 2.3 - 登录认证 🔴
- [x] 创建 `AuthController`（登录接口）
- [x] 实现 BCrypt 密码加密比对
- [x] 登录成功返回 Token + 用户信息
- [x] 登录失败返回统一错误信息

### Task 2.4 - JWT 拦截器 🔴
- [ ] 创建 `JwtInterceptor` 实现 `HandlerInterceptor`
- [ ] 拦截需要认证的接口，解析 Token 放入 ThreadLocal
- [ ] 放行白名单接口（/login、/register、静态资源）
- [ ] 在 `WebMvcConfig` 中注册拦截器

### Task 2.5 - 角色管理 🟡
- [ ] 创建 `SysRole` 实体 + Mapper + Service + Controller
- [ ] 实现角色 CRUD
- [ ] 实现角色-菜单关联接口

### Task 2.6 - 菜单管理 🟡
- [ ] 创建 `SysMenu` 实体（支持递归查询树形结构）
- [ ] 实现菜单树形查询接口
- [ ] 实现角色分配菜单接口

### Task 2.7 - 前端对接（预留） 🟢
- [ ] 提供用户当前登录信息接口（返回用户名、角色、菜单列表）
- [ ] 提供菜单列表接口（供前端动态渲染菜单）

---

## 第三阶段：基础建模模块（第 2 周）

### Task 3.1 - 产品管理 🔴
- [ ] 创建 `Product` 实体类
- [ ] 创建 `ProductMapper` + `ProductService` + `ProductController`
- [ ] 实现产品 CRUD + 分页查询 + 按分类筛选
- [ ] 实现产品编号生成规则

### Task 3.2 - 物料管理 🔴
- [ ] 创建 `Material` 实体类
- [ ] 创建 `MaterialMapper` + `MaterialService` + `MaterialController`
- [ ] 实现物料 CRUD + 分页查询 + 库存预警（低于安全库存标记）

### Task 3.3 - BOM 管理 🔴
- [ ] 创建 `Bom` 实体类 + `BomItem` 实体类
- [ ] 创建 `BomMapper` + `BomService`（含明细联查）
- [ ] 实现 BOM 新建（主表 + 明细批量新增）
- [ ] 实现 BOM 版本管理（草稿→生效→作废）
- [ ] 实现 BOM 查询（带明细列表）

### Task 3.4 - 工艺路线管理 🔴
- [ ] 创建 `ProcessRoute` 实体类 + `ProcessStep` 实体类
- [ ] 创建 `ProcessRouteMapper` + `ProcessRouteService`
- [ ] 实现工艺路线 CRUD + 工序排序管理
- [ ] 实现按产品查询工艺路线

### Task 3.5 - 车间/产线/工位管理 🟡
- [ ] 创建 `Workshop` / `ProductionLine` / `Workstation` 实体类
- [ ] 创建对应 Mapper + Service + Controller
- [ ] 实现三级关联查询（车间→产线→工位）
- [ ] 实现下拉选择接口（供其他模块使用）

---

## 第四阶段：工单管理模块（第 3 周）

### Task 4.1 - 工单创建 🔴
- [ ] 创建 `WorkOrder` 实体类
- [ ] 创建 `WorkOrderMapper` + `WorkOrderService` + `WorkOrderController`
- [ ] 实现工单创建（选择产品→自动带出 BOM 和工艺路线）
- [ ] 实现工单编号自动生成（MO + 日期 + 序号）

### Task 4.2 - 工单管理 🔴
- [ ] 实现工单列表（分页 + 多条件搜索：状态/产品/时间）
- [ ] 实现工单详情（含 BOM 用料清单、工艺路线、进度）
- [ ] 实现工单状态变更：发布→下发→完成/取消

### Task 4.3 - 生产报工 🔴
- [ ] 创建 `WorkReport` 实体类
- [ ] 创建 `WorkReportMapper` + `WorkReportService`
- [ ] 实现报工接口（按工序报工 + 合格数/不良数）
- [ ] 报工后自动更新工单 `done_qty` 进度
- [ ] 实现报工列表查询（按工单/操作员/时间）

### Task 4.4 - 领料管理 🟡
- [ ] 创建 `MaterialIssue` + `MaterialIssueItem` 实体类
- [ ] 实现领料申请（按工单 BOM 自动生成清单）
- [ ] 实现领料审批流程
- [ ] 实现发料扣减库存

### Task 4.5 - 退料管理 🟡
- [ ] 创建 `MaterialReturn` 实体类
- [ ] 实现退料流程（退料→入库）

---

## 第五阶段：质量管理模块（第 4 周）

### Task 5.1 - QC 标准管理 🔴
- [ ] 创建 `QcStandard` 实体类
- [ ] 创建 `QcStandardMapper` + `QcService` + `QcController`
- [ ] 实现 QC 标准 CRUD（按产品+工序配置）

### Task 5.2 - 质检执行 🔴
- [ ] 创建 `QcRecord` + `QcRecordDetail` 实体类
- [ ] 实现质检任务触发（报工后自动创建）
- [ ] 实现质检执行（按标准逐项检验 + 实测值录入）
- [ ] 自动判定 PASS/FAIL

### Task 5.3 - 产品追溯 🔴
- [ ] 创建 `Traceability` 实体类
- [ ] 实现 SN 码追溯查询接口
- [ ] 追溯信息聚合：工单+工艺+工位+操作员+物料+质检

---

## 第六阶段：设备与统计模块（第 5 周）

### Task 6.1 - 设备管理 🔴
- [ ] 创建 `Equipment` 实体类
- [ ] 创建 `EquipmentMapper` + `EquipmentService` + `EquipmentController`
- [ ] 实现设备 CRUD + 状态管理

### Task 6.2 - 保养管理 🟡
- [ ] 创建 `MaintainRecord` 实体类
- [ ] 实现保养记录 CRUD
- [ ] 实现下次保养日期提醒

### Task 6.3 - 故障管理 🟡
- [ ] 创建 `EquipmentFault` 实体类
- [ ] 实现故障报修 + 维修记录

### Task 6.4 - 统计报表 🟡
- [ ] 创建 `ReportDaily` 实体类
- [ ] 实现生产日报统计（按车间/产线/日期）
- [ ] 良品率计算

---

## 第七阶段：优化与完善（第 6 周）

### Task 7.1 - MyBatis-Plus 增强 🔴
- [ ] 自动填充：`created_at` / `updated_at`（MetaObjectHandler）
- [ ] 逻辑删除：`deleted` 字段（@TableLogic）
- [ ] 自动填充操作人：从 JWT 中获取当前用户 ID

### Task 7.2 - 接口优化 🟡
- [ ] 统一接口返回格式（已实现 Result）
- [ ] 统一异常处理（已实现）
- [ ] 添加 Swagger/Knife4j 接口文档

### Task 7.3 - 代码规范 🟡
- [ ] 补充所有实体类注释
- [ ] 补充所有 Service 方法注释
- [ ] 统一命名规范审查

### Task 7.4 - 性能优化 🟢
- [ ] 高频查询加索引（已建表时处理）
- [ ] 列表接口加分页（PageHelper / MyBatis-Plus 分页插件）
- [ ] BOM 联查用 resultMap 避免 N+1 问题

### Task 7.5 - 部署准备 🟢
- [ ] 生产环境配置文件 application-prod.yml
- [ ] 关闭 debug 日志
- [ ] 关闭 CSRF 关闭（前后端分离）
- [ ] 打包测试 `mvn clean package`

---

## 📊 任务统计

| 模块 | P0 任务 | P1 任务 | P2 任务 | 合计 |
|------|---------|---------|---------|------|
| 项目搭建 | 3 | 0 | 0 | 3 |
| 权限管理 | 4 | 3 | 1 | 8 |
| 基础建模 | 4 | 1 | 0 | 5 |
| 工单管理 | 3 | 2 | 0 | 5 |
| 质量管理 | 3 | 0 | 0 | 3 |
| 设备统计 | 1 | 3 | 1 | 5 |
| 优化完善 | 2 | 2 | 2 | 6 |
| **总计** | **20** | **11** | **4** | **35** |

---

## 🎯 核心路径（必须完成才能演示系统）

```
用户登录 → 产品管理 → 物料管理 → BOM管理 → 工艺路线 → 
工单创建 → 工单下发 → 生产报工 → 质检记录 → 追溯查询
```

这 10 个任务是系统演示的**最小可用集合**，优先完成。

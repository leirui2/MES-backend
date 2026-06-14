-- =============================================
-- 初始化测试用户数据（共 10 个）
-- 密码统一为：123456（BCrypt 加密）
-- =============================================

-- BCrypt($2a$10$N9qo8uLOickgx2ZMRZoMye) = 123456
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `phone`, `email`, `status`, `dept_id`, `created_at`, `updated_at`, `is_delete`) VALUES
(2,  'zhangsan',  '$2a$10$N9qo8uLOickgx2ZMRZoMye', '张三',   '13800001001', 'zhangsan@mes.com',   1, 1, '2026-06-14 10:00:00', '2026-06-14 10:00:00', 0),
(3,  'lisi',      '$2a$10$N9qo8uLOickgx2ZMRZoMye', '李四',   '13800001002', 'lisi@mes.com',       1, 1, '2026-06-14 10:00:00', '2026-06-14 10:00:00', 0),
(4,  'wangwu',    '$2a$10$N9qo8uLOickgx2ZMRZoMye', '王五',   '13800001003', 'wangwu@mes.com',     1, 2, '2026-06-14 10:00:00', '2026-06-14 10:00:00', 0),
(5,  'zhaoliu',   '$2a$10$N9qo8uLOickgx2ZMRZoMye', '赵六',   '13800001004', 'zhaoliu@mes.com',    1, 2, '2026-06-14 10:00:00', '2026-06-14 10:00:00', 0),
(6,  'sunqi',     '$2a$10$N9qo8uLOickgx2ZMRZoMye', '孙七',   '13800001005', 'sunqi@mes.com',      1, 3, '2026-06-14 10:00:00', '2026-06-14 10:00:00', 0),
(7,  'zhouba',    '$2a$10$N9qo8uLOickgx2ZMRZoMye', '周八',   '13800001006', 'zhouba@mes.com',     1, 3, '2026-06-14 10:00:00', '2026-06-14 10:00:00', 0),
(8,  'wujiu',     '$2a$10$N9qo8uLOickgx2ZMRZoMye', '吴九',   '13800001007', 'wujiu@mes.com',      1, 4, '2026-06-14 10:00:00', '2026-06-14 10:00:00', 0),
(9,  'zhengshi','$2a$10$N9qo8uLOickgx2ZMRZoMye', '郑十',   '13800001008', 'zhengshi@mes.com',   1, 4, '2026-06-14 10:00:00', '2026-06-14 10:00:00', 0),
(10, 'liuwu',     '$2a$10$N9qo8uLOickgx2ZMRZoMye', '刘五',   '13800001009', 'liuwu@mes.com',      0, 5, '2026-06-14 10:00:00', '2026-06-14 10:00:00', 0),
(11, 'chener',    '$2a$10$N9qo8uLOickgx2ZMRZoMye', '陈二',   '13800001010', 'chener@mes.com',     1, 5, '2026-06-14 10:00:00', '2026-06-14 10:00:00', 0);

-- 用户角色分配
-- zhangsan, lisi → 生产经理(MANAGER)
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (2, 2), (3, 2);

-- wangwu, zhaoliu → 班组长(LEADER)
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (4, 3), (5, 3);

-- sunqi, zhouba → 操作工(OPERATOR)
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (6, 4), (7, 4);

-- wujiu, zhengshi → 质检员(QC)
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (8, 5), (9, 5);

-- liuwu(禁用), chener → 仓管员(WAREHOUSE)
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (10, 6), (11, 6);

-- 角色菜单权限分配
-- 生产经理(MANAGER, role_id=2) → 产品+工单+物料+报表菜单
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, id FROM `sys_menu` WHERE id IN (2, 3, 4, 6, 7);

-- 操作工(OPERATOR, role_id=4) → 仅生产报工菜单
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 4, id FROM `sys_menu` WHERE id IN (5);

-- 质检员(QC, role_id=5) → 质量管理菜单
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 5, id FROM `sys_menu` WHERE id IN (6);

-- 仓管员(WAREHOUSE, role_id=6) → 物料管理菜单
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 6, id FROM `sys_menu` WHERE id IN (3);

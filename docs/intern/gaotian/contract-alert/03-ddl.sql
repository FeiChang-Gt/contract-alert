
-- 试题4：合同到期预警主表（对齐统一规范）
DROP TABLE IF EXISTS `esd_contract_alert`;
-- 表3：合同到期预警台账表
CREATE TABLE `esd_contract_alert` (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                      `emp_no` varchar(50) NOT NULL COMMENT '员工工号',
                                      `emp_name` varchar(100) NOT NULL COMMENT '员工姓名',
                                      `dept_name` varchar(100) DEFAULT NULL COMMENT '所属部门',
                                      `contract_start` date NOT NULL COMMENT '合同开始日期',
                                      `contract_end` date NOT NULL COMMENT '合同到期日期',
                                      `remain_days` int DEFAULT NULL COMMENT '距离到期剩余天数',
                                      `alert_level` tinyint DEFAULT 1 COMMENT '预警等级：1-正常，2-预警，3-紧急',
                                      `is_renewed` tinyint DEFAULT 0 COMMENT '跟进状态：0-未续签，1-已续签，2-不再续签',
                                      `follow_note` varchar(255) DEFAULT NULL COMMENT '跟进备注（不再续签必填）',
                                      `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      `create_by` bigint DEFAULT NULL COMMENT '创建人',
                                      `update_by` bigint DEFAULT NULL COMMENT '更新人',
                                      `del_flag` tinyint DEFAULT 0 COMMENT '逻辑删除：0-正常，1-已删除',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      UNIQUE KEY `uk_emp_no` (`emp_no`) USING BTREE,
                                      KEY `idx_alert_level` (`alert_level`) USING BTREE,
                                      KEY `idx_contract_end` (`contract_end`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工合同到期预警台账表' ROW_FORMAT = Dynamic;

-- 插入数据方便分页查询
INSERT INTO esd_contract_alert (
    emp_no, emp_name, dept_name,
    contract_start, contract_end,
    remain_days, alert_level,
    is_renewed, follow_note, follow_status,
    create_by, update_by, del_flag
)
VALUES
    ('10001', '张三', '技术部', '2025-01-01', '2026-12-31', 220, 1, 0, '', '待跟进', 'admin', 'admin', 0),
    ('10002', '李四', '产品部', '2025-02-15', '2026-07-15', 49, 1, 0, '', '待跟进', 'admin', 'admin', 0),
    ('10003', '王五', '测试部', '2025-03-10', '2026-06-20', 24, 2, 0, '员工考虑续签', '跟进中', 'admin', 'admin', 0),
    ('10004', '赵六', '人事部', '2025-04-01', '2026-06-05', 9, 3, 0, '计划续签', '已沟通', 'admin', 'admin', 0),
    ('10005', '钱七', '财务部', '2025-05-01', '2026-08-01', 66, 1, 0, '', '待跟进', 'admin', 'admin', 0),
    ('10006', '孙八', '市场部', '2025-06-01', '2026-06-10', 14, 2, 0, '', '跟进中', 'admin', 'admin', 0),
    ('10007', '周九', '运营部', '2025-07-01', '2026-06-01', 5, 3, 0, '员工不续签', '已完成', 'admin', 'admin', 0),
    ('10008', '吴十', '技术部', '2025-08-01', '2026-10-01', 127, 1, 0, '', '待跟进', 'admin', 'admin', 0),
    ('10009', '郑十一', '产品部', '2025-09-01', '2026-06-25', 29, 2, 0, '', '跟进中', 'admin', 'admin', 0),
    ('10010', '王十二', '测试部', '2025-10-01', '2026-06-02', 6, 3, 0, '待沟通', '跟进中', 'admin', 'admin', 0),
    ('10011', '刘十三', '技术部', '2025-11-01', '2026-11-01', 158, 1, 0, '', '待跟进', 'admin', 'admin', 0),
    ('10012', '陈十四', '市场部', '2025-12-01', '2026-07-01', 35, 1, 0, '', '待跟进', 'admin', 'admin', 0),
    ('10013', '林十五', '财务部', '2025-01-10', '2026-06-15', 19, 2, 1, '已续签', '已完成', 'admin', 'admin', 0),
    ('10014', '胡十六', '运营部', '2025-02-20', '2026-06-03', 7, 3, 0, '不续签', '已完成', 'admin', 'admin', 0),
    ('10015', '朱十七', '人事部', '2025-04-05', '2026-09-05', 99, 1, 0, '', '待跟进', 'admin', 'admin', 0);

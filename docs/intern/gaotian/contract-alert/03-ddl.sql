
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

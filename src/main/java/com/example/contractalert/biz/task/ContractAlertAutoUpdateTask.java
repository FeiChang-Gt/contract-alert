package com.example.contractalert.biz.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.contractalert.biz.Entity.EsdContractAlert;
import com.example.contractalert.biz.Mapper.ContractMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContractAlertAutoUpdateTask {

    private final ContractMapper contractMapper;

    /**
     * 每天凌晨 00:00 自动更新
     * 测试用：每秒执行一次（写完再改回凌晨）
     * @Scheduled(fixedRate = 1000)
     */
    //秒分时日月周
    @Scheduled(cron = "0 0 0 * * ?")
    public void autoUpdateAlertLevel() {
        log.info("⏰ 开始自动更新合同预警等级");

        try {
            // 1. 查询未删除的数据
            LambdaQueryWrapper<EsdContractAlert> query = new LambdaQueryWrapper<>();
            query.eq(EsdContractAlert::getDelFlag, 0);
            List<EsdContractAlert> list = contractMapper.selectList(query);

            LocalDate now = LocalDate.now();

            // 2. 循环更新每一条
            for (EsdContractAlert contract : list) {
                LocalDate end = contract.getContractEnd();

                // ======================== 关键修复 ========================
                // 跳过空日期，防止整个任务崩掉
                if (end == null) {
                    continue;
                }

                // 计算剩余天数
                long remainDays = ChronoUnit.DAYS.between(now, end);

                // 计算等级
                int level;
                if (remainDays > 30) {
                    level = 1;
                } else if (remainDays >= 15) {
                    level = 2;
                } else {
                    level = 3;
                }

                //最关键：同时更新 剩余天数 + 等级
                LambdaUpdateWrapper<EsdContractAlert> update = new LambdaUpdateWrapper<>();
                update.eq(EsdContractAlert::getId, contract.getId())
                        .set(EsdContractAlert::getRemainDays, (int) remainDays)
                        .set(EsdContractAlert::getAlertLevel, level);

                contractMapper.update(null, update);
            }

            log.info("✅ 更新完成，共处理 {} 条数据", list.size());

        } catch (Exception e) {
            log.error("❌ 自动更新预警失败", e);
        }
    }
}
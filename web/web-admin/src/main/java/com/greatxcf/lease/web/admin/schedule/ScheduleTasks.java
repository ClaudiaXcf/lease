package com.greatxcf.lease.web.admin.schedule;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.greatxcf.lease.model.entity.LeaseAgreement;
import com.greatxcf.lease.model.enums.LeaseStatus;
import com.greatxcf.lease.web.admin.service.LeaseAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

// 声明为 Spring 组件，由容器扫描并管理
@Component
public class ScheduleTasks {

    // 注入租约服务，用于更新租约状态
    @Autowired
    private LeaseAgreementService leaseAgreementService;

    /**
     * 每天 00:00:00 检查并将租约结束日期已过的租约状态更新为「已到期」。
     * 仅处理「已签约」「退租待确认」两种签约中的状态。
     */
    // 定时规则：秒 分 时 日 月 周；0 0 0 表示每天 0 点 0 分 0 秒
    @Scheduled(cron = "0 0 0 * * ?")
    public void expireLeaseAgreements() {
        // 获取当前日期（仅日期，无时分秒）
        LocalDate today = LocalDate.now();
        // 将「今天」转为今天 0 点 0 分 0 秒的 Date，用于与租约结束日期比较
        Date todayStart = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        System.out.println(todayStart);
        // 构建更新条件包装器（Lambda 方式，类型安全）
        LambdaUpdateWrapper<LeaseAgreement> wrapper = new LambdaUpdateWrapper<>();
        // 条件：租约结束日期 < 今天 0 点（即已过期）
        wrapper.lt(LeaseAgreement::getLeaseEndDate, todayStart)
                // 且状态为「已签约」或「退租待确认」
                .in(LeaseAgreement::getStatus, LeaseStatus.SIGNED, LeaseStatus.WITHDRAWING)
                // 将满足条件的记录状态更新为「已到期」
                .set(LeaseAgreement::getStatus, LeaseStatus.EXPIRED);
        // 执行批量更新
        leaseAgreementService.update(wrapper); 
    }
}

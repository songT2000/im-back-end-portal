package com.im.scheduler.task;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * API代付订单同步
 *
 * @author Barry
 * @date 2020-07-18
 */
@Component
public class ApiWithdrawOrderSyncTask {
    private static final Log LOG = LogFactory.get();
    private WithdrawService withdrawService;

    @Autowired
    public void setWithdrawService(WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }

    @Scheduled(fixedRate = 600 * 1000)
    private void task() {
        start();
    }

    private void start() {
        try {
            // 仅查找过去12小时的订单，并与三方进行同步
            LocalDateTime startTime = LocalDateTime.now().minusHours(12);
            LocalDateTime endTime = LocalDateTime.now().minusMinutes(5);

            withdrawService.syncStatusFromApi(startTime, endTime);
        } catch (Exception e) {
            LOG.error(e, "执行[API代付订单状态同步]异常");
        }
    }
}

package com.im.scheduler.task;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 三方充值订单同步
 *
 * @author Barry
 * @date 2020-07-18
 */
@Component
public class ApiRechargeOrderSyncTask {
    private static final Log LOG = LogFactory.get();
    private RechargeService rechargeService;

    @Autowired
    public void setRechargeService(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    @Scheduled(fixedRate = 600 * 1000)
    private void task() {
        start();
    }

    private void start() {
        try {
            // 仅查找过去30分钟前～5分钟前的订单，并与三方进行同步
            LocalDateTime startTime = LocalDateTime.now().minusMinutes(30);
            LocalDateTime endTime = LocalDateTime.now().minusMinutes(5);

            rechargeService.syncStatusFromApi(startTime, endTime);
        } catch (Exception e) {
            LOG.error(e, "执行[三方充值订单状态同步]异常");
        }
    }
}

package com.im.scheduler.task;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.service.TimGroupService;
import com.im.common.service.TimMessageC2cService;
import com.im.common.util.instant.InstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 同步单聊信息
 * <br>每天凌晨5点30执行一次
 */
@Component
public class C2cMessageSyncTask {
    private static final Log LOG = LogFactory.get();

    private TimMessageC2cService timMessageC2cService;

    @Autowired
    public void setTimMessageC2cService(TimMessageC2cService timMessageC2cService) {
        this.timMessageC2cService = timMessageC2cService;
    }

    @Scheduled(cron = "0 30 5 * * ?")
    private void task() {
        start();
    }

    private void start() {
        try {
            InstantUtil.start();

            timMessageC2cService.sync();

            InstantUtil.end("完成执行[同步单聊信息]");
        } catch (Exception e) {
            LOG.error(e, "执行[同步单聊信息]失败");
        }
    }
}

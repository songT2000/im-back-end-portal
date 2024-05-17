package com.im.scheduler.task;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.service.TimMessageGroupService;
import com.im.common.service.TimOperationStatisticService;
import com.im.common.util.instant.InstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * app运营数据同步任务
 * <br>每天凌晨6点30执行一次,太早的话可能还没生成前一天的统计数据
 */
@Component
public class AppOperationTask {
    private static final Log LOG = LogFactory.get();

    private TimOperationStatisticService timOperationStatisticService;

    @Autowired
    public void setTimOperationStatisticService(TimOperationStatisticService timOperationStatisticService) {
        this.timOperationStatisticService = timOperationStatisticService;
    }

    @Scheduled(cron = "0 30 6 * * ?")
    private void task() {
        start();
    }

    private void start() {
        try {
            InstantUtil.start();

            timOperationStatisticService.sync();

            InstantUtil.end("完成执行[app运营数据同步任务]");
        } catch (Exception e) {
            LOG.error(e, "执行[app运营数据同步任务]失败");
        }
    }
}

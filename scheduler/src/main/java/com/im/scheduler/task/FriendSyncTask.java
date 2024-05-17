package com.im.scheduler.task;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.service.TimBlacklistService;
import com.im.common.service.TimFriendService;
import com.im.common.service.TimOperationStatisticService;
import com.im.common.util.instant.InstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 用户关系链数据同步任务
 * <br>每小时执行一次
 */
@Component
public class FriendSyncTask {
    private static final Log LOG = LogFactory.get();

    private TimFriendService timFriendService;
    private TimBlacklistService timBlacklistService;

    @Autowired
    public void setTimFriendService(TimFriendService timFriendService) {
        this.timFriendService = timFriendService;
    }

    @Autowired
    public void setTimBlacklistService(TimBlacklistService timBlacklistService) {
        this.timBlacklistService = timBlacklistService;
    }

    @Async
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void task() {
        start();
    }

    private void start() {
        try {
            InstantUtil.start();

            timFriendService.sync();

            timBlacklistService.sync();

            InstantUtil.end("完成执行[用户关系链数据同步任务]");
        } catch (Exception e) {
            LOG.error(e, "执行[用户关系链数据同步任务]失败");
        }
    }
}

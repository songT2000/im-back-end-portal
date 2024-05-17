package com.im.scheduler.task;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.service.TimGroupService;
import com.im.common.service.UserOperationLogService;
import com.im.common.util.StrUtil;
import com.im.common.util.instant.InstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 同步群组信息
 * <br>每小时执行一次
 */
@Component
public class GroupInfoSyncTask {
    private static final Log LOG = LogFactory.get();

    private TimGroupService timGroupService;

    @Autowired
    public void setTimGroupService(TimGroupService timGroupService) {
        this.timGroupService = timGroupService;
    }

    @Async
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void task() {
        start();
    }

    private void start() {
        try {
            InstantUtil.start();

            timGroupService.sync();

            InstantUtil.end("完成执行[同步群组信息]");
        } catch (Exception e) {
            LOG.error(e, "执行[同步群组信息]失败");
        }
    }
}

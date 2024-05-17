package com.im.scheduler.task;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.service.TimGroupMemberService;
import com.im.common.service.TimGroupService;
import com.im.common.service.TimMessageGroupService;
import com.im.common.util.instant.InstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 同步群组聊天信息
 * <br>每小时执行一次
 */
@Component
public class GroupMessageSyncTask {
    private static final Log LOG = LogFactory.get();

    private TimMessageGroupService timMessageGroupService;

    @Autowired
    public void setTimMessageGroupService(TimMessageGroupService timMessageGroupService) {
        this.timMessageGroupService = timMessageGroupService;
    }

    @Async
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void task() {
        start();
    }

    private void start() {
        try {
            InstantUtil.start();

            timMessageGroupService.sync();

            InstantUtil.end("完成执行[同步群组聊天信息]");
        } catch (Exception e) {
            LOG.error(e, "执行[同步群组聊天信息]失败");
        }
    }
}

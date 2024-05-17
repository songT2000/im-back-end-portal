package com.im.scheduler.task;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.service.PortalUserProfileService;
import com.im.common.service.TimUserDeviceStateService;
import com.im.common.util.instant.InstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 用户资料数据同步任务
 * <br>每10分钟执行一次
 */
@Component
public class PortalUserProfileSyncTask {
    private static final Log LOG = LogFactory.get();

    private PortalUserProfileService portalUserProfileService;

    private TimUserDeviceStateService timUserDeviceStateService;

    @Autowired
    public void setPortalUserProfileService(PortalUserProfileService portalUserProfileService) {
        this.portalUserProfileService = portalUserProfileService;
    }

    @Autowired
    public void setTimUserDeviceStateService(TimUserDeviceStateService timUserDeviceStateService) {
        this.timUserDeviceStateService = timUserDeviceStateService;
    }

    @Scheduled(fixedRate = 60 * 1000 * 10)
    private void task() {
        start();
    }

    private void start() {
        try {
            InstantUtil.start();

            portalUserProfileService.syncFromSdk();

            timUserDeviceStateService.syncStateFromSdk();

            InstantUtil.end("完成执行[SDK同步用户资料任务]");
        } catch (Exception e) {
            LOG.error(e, "执行[SDK同步用户资料]失败");
        }
    }
}

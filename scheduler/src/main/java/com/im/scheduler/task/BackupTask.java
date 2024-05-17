package com.im.scheduler.task;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.scheduler.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 数据备份任务
 *
 * @author Barry
 * @date 2020-01-02
 */
@Component
public class BackupTask {
    private static final Log LOG = LogFactory.get();

    private BackupService backupService;

    @Autowired
    public void setBackupService(BackupService backupService) {
        this.backupService = backupService;
    }

    @Scheduled(cron = "0 0 5 * * ?")
    private void task() {
        start();
    }

    private void start() {
        try {
            LOG.info("开始执行[数据备份任务]");

            backupService.backupAllData();

            LOG.info("完成执行[数据备份任务]");
        } catch (Exception e) {
            LOG.error(e, "执行[数据备份任务]失败");
        }
    }
}

package com.im.scheduler.task;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.service.UserOperationLogService;
import com.im.common.util.StrUtil;
import com.im.common.util.instant.InstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 处理用户操作日志
 *
 * @author Barry
 * @date 2019-11-26
 */
@Component
public class UserOperationLogTask {
    private static final Log LOG = LogFactory.get();

    private static final int MAX_BATCH_NUM = 500;

    private UserOperationLogService userOperationLogService;

    @Autowired
    public void setUserActionLogService(UserOperationLogService userOperationLogService) {
        this.userOperationLogService = userOperationLogService;
    }

    @Scheduled(fixedRate = 20 * 1000)
    private void task() {
        start();
    }

    private void start() {
        try {
            InstantUtil.start();

            int count = userOperationLogService.processQueen(MAX_BATCH_NUM);

            if (count > 0) {
                String remark = StrUtil.format("完成执行[用户操作日志入库]，共成功处理{}条", count);
                InstantUtil.end(remark);
            }
        } catch (Exception e) {
            LOG.error(e, "执行[用户操作日志入库]失败");
        }
    }
}

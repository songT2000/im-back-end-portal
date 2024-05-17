package com.im.scheduler.task;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.service.GroupRedEnvelopeService;
import com.im.common.service.PersonalRedEnvelopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 红包过期任务
 *
 * @author Barry
 * @date 2021-12-20
 */
@Component
public class RedEnvelopeExpireTask {
    private static final Log LOG = LogFactory.get();

    private PersonalRedEnvelopeService personalRedEnvelopeService;
    private GroupRedEnvelopeService groupRedEnvelopeService;

    @Autowired
    public void setPersonalRedEnvelopeService(PersonalRedEnvelopeService personalRedEnvelopeService) {
        this.personalRedEnvelopeService = personalRedEnvelopeService;
    }

    @Autowired
    public void setGroupRedEnvelopeService(GroupRedEnvelopeService groupRedEnvelopeService) {
        this.groupRedEnvelopeService = groupRedEnvelopeService;
    }

    @Scheduled(fixedRate = 600 * 1000)
    private void task() {
        start();
    }

    private void start() {
        try {
            personalRedEnvelopeService.checkExpired();
        } catch (Exception e) {
            LOG.error(e, "执行[个人红包过期任务]失败");
        }

        try {
            groupRedEnvelopeService.checkExpired();
        } catch (Exception e) {
            LOG.error(e, "执行[群红包过期任务]失败");
        }
    }
}

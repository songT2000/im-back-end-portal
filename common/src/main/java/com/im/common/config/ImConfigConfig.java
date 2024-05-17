package com.im.common.config;

import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.ImConfigBO;
import com.im.common.util.spring.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * IM配置
 *
 * @author Barry
 * @date 2021-12-21
 */
@Component
public class ImConfigConfig {
    @Scheduled(cron = "0 0 6 ? * MON")
    private void task() {
        // 每周不繁忙的时候再renew一个
        SysConfigCache sysConfigCache = SpringContextUtil.getBean(SysConfigCache.class);
        ImConfigBO imConfig = sysConfigCache.getImConfigFromLocal();
        imConfig.renewSig();
    }
}

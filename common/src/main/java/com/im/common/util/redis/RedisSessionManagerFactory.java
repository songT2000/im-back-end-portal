package com.im.common.util.redis;

import com.im.common.entity.enums.PortalTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 用户session，提供选择
 *
 * @author Barry
 * @date 2020-09-30
 */
@Component
@ConditionalOnBean({PortalRedisSessionManager.class, AdminRedisSessionManager.class})
public class RedisSessionManagerFactory {
    private PortalRedisSessionManager portalRedisSessionManager;
    private AdminRedisSessionManager adminRedisSessionManager;

    @Autowired
    public RedisSessionManagerFactory(PortalRedisSessionManager portalRedisSessionManager,
                                      AdminRedisSessionManager adminRedisSessionManager) {
        Assert.notNull(portalRedisSessionManager, "portalRedisSessionManager不能为空");
        Assert.notNull(adminRedisSessionManager, "adminRedisSessionManager不能为空");

        this.portalRedisSessionManager = portalRedisSessionManager;
        this.adminRedisSessionManager = adminRedisSessionManager;
    }

    /**
     * 获取某类型用户redis session容器
     *
     * @param portalType
     * @return
     */
    public RedisSessionManager getSessionManager(PortalTypeEnum portalType) {
        if (portalType == PortalTypeEnum.PORTAL) {
            return portalRedisSessionManager;
        } else if (portalType == PortalTypeEnum.ADMIN) {
            return adminRedisSessionManager;
        }
        return null;
    }
}

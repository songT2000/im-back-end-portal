package com.im.portal.config;

import com.im.common.cache.impl.PortalAreaBlackWhiteCache;
import com.im.common.cache.impl.PortalIpBlackWhiteCache;
import com.im.common.response.RestResponse;
import com.im.common.util.RequestUtil;
import com.im.common.util.ResponseUtil;
import com.im.common.util.redis.PortalRedisSessionManager;
import com.im.common.vo.PortalSessionUser;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 检查每一次操作的IP
 *
 * @author Barry
 * @date 2019-10-12
 */
public class IpCheckerInterceptor implements HandlerInterceptor {
    private PortalIpBlackWhiteCache portalIpBlackWhiteCache;
    private PortalAreaBlackWhiteCache portalAreaBlackWhiteCache;
    private PortalRedisSessionManager portalRedisSessionManager;

    public IpCheckerInterceptor(PortalIpBlackWhiteCache portalIpBlackWhiteCache,
                                PortalAreaBlackWhiteCache portalAreaBlackWhiteCache,
                                PortalRedisSessionManager portalRedisSessionManager) {
        this.portalIpBlackWhiteCache = portalIpBlackWhiteCache;
        this.portalAreaBlackWhiteCache = portalAreaBlackWhiteCache;
        this.portalRedisSessionManager = portalRedisSessionManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = RequestUtil.getRequestIp(request);

        // IP
        {
            PortalSessionUser sessionUser = portalRedisSessionManager.getSessionUser(request);
            String username = sessionUser == null ? null : sessionUser.getUsername();

            RestResponse rsp = portalIpBlackWhiteCache.checkIpBlackWhite(username, ip);
            if (!rsp.isOkRsp()) {
                ResponseUtil.printJson(response, rsp);
                return false;
            }
        }

        // 区域
        {
            RestResponse rsp = portalAreaBlackWhiteCache.checkAreaBlackWhite(ip);
            if (!rsp.isOkRsp()) {
                ResponseUtil.printJson(response, rsp);
                return false;
            }
        }

        return true;
    }
}

package com.im.admin.config;

import com.im.common.cache.impl.AdminIpBlackWhiteCache;
import com.im.common.response.RestResponse;
import com.im.common.util.RequestUtil;
import com.im.common.util.ResponseUtil;
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
    private AdminIpBlackWhiteCache adminIpBlackWhiteCache;

    public IpCheckerInterceptor(AdminIpBlackWhiteCache adminIpBlackWhiteCache) {
        this.adminIpBlackWhiteCache = adminIpBlackWhiteCache;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = RequestUtil.getRequestIp(request);

        // IP
        {
            RestResponse rsp = adminIpBlackWhiteCache.checkIpBlackWhite(ip);
            if (!rsp.isOkRsp()) {
                ResponseUtil.printJson(response, rsp);
                return false;
            }
        }

        return true;
    }
}

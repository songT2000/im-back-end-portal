package com.im.common.util.redis;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.util.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * session自动延长时效性
 *
 * @author Barry
 * @date 2019-10-12
 */
public class SessionRefreshInterceptor implements HandlerInterceptor {
    private RedisSessionManager redisSessionManager;

    private static final Log LOG = LogFactory.get();

    public SessionRefreshInterceptor(RedisSessionManager redisSessionManager) {
        this.redisSessionManager = redisSessionManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 解析token
        // String token = JwtUtil.getTokenFromHeader(request);
        // LOG.info("token:{}",token);
        redisSessionManager.refreshSession(request);
        return true;
    }
}

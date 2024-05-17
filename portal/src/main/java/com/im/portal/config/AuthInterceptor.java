package com.im.portal.config;

import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.service.UserAuthTokenService;
import com.im.common.vo.PortalSessionUser;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截器，自动登录
 *
 * <ul>
 *     <li>检查token有效性</li>
 *     <li>自动使用token登录</li>
 * </ul>
 *
 * @author Barry
 * @date 2019-10-12
 */
public class AuthInterceptor implements HandlerInterceptor {
    private UserAuthTokenService authTokenService;

    public AuthInterceptor(UserAuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        PortalSessionUser sessionUser = authTokenService.checkToken(request, response, PortalTypeEnum.PORTAL);

        return sessionUser != null;
    }
}

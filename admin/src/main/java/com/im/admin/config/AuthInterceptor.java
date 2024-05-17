package com.im.admin.config;

import cn.hutool.core.util.StrUtil;
import com.im.admin.config.permission.CheckPermission;
import com.im.common.cache.impl.AdminRoleMenuCache;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.AdminRole;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.UserAuthTokenService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.RequestUtil;
import com.im.common.util.ResponseUtil;
import com.im.common.vo.AdminSessionUser;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 权限拦截器，自动登录、检查是否有权限访问接口
 * 在需要权限的Controller方法上加上{@link CheckPermission CheckPermission}注解
 *
 * <ul>
 *     <li>检查token有效性</li>
 *     <li>自动使用token登录</li>
 *     <li>检查是否有权限访问接口</li>
 * </ul>
 *
 * @author Barry
 * @date 2019-10-12
 */
public class AuthInterceptor implements HandlerInterceptor {
    private UserAuthTokenService authTokenService;
    private AdminRoleMenuCache roleMenuCache;

    public AuthInterceptor(UserAuthTokenService authTokenService, AdminRoleMenuCache roleMenuCache) {
        this.authTokenService = authTokenService;
        this.roleMenuCache = roleMenuCache;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AdminSessionUser sessionUser = authTokenService.checkToken(request, response, PortalTypeEnum.ADMIN);
        if (sessionUser == null) {
            return false;
        }

        // 检查权限
        boolean hasPermission = checkPermission(sessionUser, response, handler);
        if (!hasPermission) {
            return false;
        }

        return true;
    }

    private boolean checkPermission(AdminSessionUser sessionUser, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod method = (HandlerMethod) handler;
        CheckPermission checkPermission = method.getMethodAnnotation(CheckPermission.class);
        if (checkPermission == null) {
            return true;
        }

        // 没角色任何权限不能访问
        List<AdminRole> userRoles = sessionUser.getRoles();
        if (CollectionUtil.isEmpty(userRoles)) {
            ResponseUtil.printJson(response, RestResponse.failed(ResponseCode.SYS_NO_PERMISSION));
            return false;
        }

        // 管理员直接通行
        if (hasAdminRole(userRoles)) {
            return true;
        }

        boolean hasPermission = false;

        if (StrUtil.isAllBlank(checkPermission.url())) {
            // 通过URL检查
            String url = RequestUtil.getCurrentRequestPath();
            hasPermission = roleMenuCache.hasPermissionByMenuUrl(userRoles, url);
        } else {
            // 通过指定的url进行检查
            for (String url : checkPermission.url()) {
                hasPermission = roleMenuCache.hasPermissionByMenuUrl(userRoles, url);
                if (hasPermission) {
                    break;
                }
            }
        }

        if (hasPermission) {
            // 有权限
            return true;
        } else {
            // 没有权限
            ResponseUtil.printJson(response, RestResponse.failed(ResponseCode.SYS_NO_PERMISSION));
            return false;
        }
    }

    private boolean hasAdminRole(List<AdminRole> userRoles) {
        boolean hasAdminRole = CollectionUtil.anyMatch(userRoles, adminRole -> adminRole != null && adminRole.getParentId() == CommonConstant.LONG_0);
        return hasAdminRole;
    }
}

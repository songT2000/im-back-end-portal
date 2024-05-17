package com.im.admin.controller;

import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.util.redis.AdminRedisSessionManager;
import com.im.common.vo.AdminSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller父类,带有一些方便使用的方法
 *
 * @author Barry
 * @date 2018/5/14
 */
@Validated
public abstract class BaseController {
    protected AdminRedisSessionManager adminRedisSessionManager;

    @Autowired
    public void setAdminRedisSessionManager(AdminRedisSessionManager adminRedisSessionManager) {
        this.adminRedisSessionManager = adminRedisSessionManager;
    }

    protected RestResponse ok() {
        return RestResponse.OK;
    }

    protected <T> RestResponse ok(T data) {
        return RestResponse.ok(data);
    }

    protected RestResponse failed(ResponseCode code) {
        return RestResponse.failed(code);
    }

    protected RestResponse failed(ResponseCode code, Object... params) {
        return RestResponse.failed(code, params);
    }

    protected AdminSessionUser getSessionUser(HttpServletRequest request) {
        return adminRedisSessionManager.getSessionUser(request);
    }
}

package com.im.common.util.servlet;

import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

/**
 * Servlet配置
 *
 * @author Barry
 * @date 2020-05-26
 */
public class ServletConfigUtil extends SpringBootServletInitializer implements ErrorController {
    private ErrorAttributes errorAttributes;

    @Autowired
    public void setErrorAttributes(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
        SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
        sessionCookieConfig.setHttpOnly(true);
    }

    @RequestMapping(
            value = {"/error"},
            produces = {"application/json;charset=UTF-8"}
    )
    @ResponseStatus(HttpStatus.OK)
    public Object errorHtml(HttpServletRequest request, HttpServletResponse response) {
        return handleAjaxError(request, response);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    private RestResponse handleAjaxError(HttpServletRequest request, HttpServletResponse response) {
        int code = response.getStatus();
        if (HttpStatus.NOT_FOUND.value() == code) {
            String requestPath = RequestUtil.getRequestPath(request);
            return RestResponse.failed(ResponseCode.SYS_NO_SUCH_METHOD, requestPath);
        } else if (HttpStatus.FORBIDDEN.value() == code) {
            return RestResponse.failed(ResponseCode.SYS_NO_PERMISSION);
        } else if (HttpStatus.UNAUTHORIZED.value() == code) {
            return RestResponse.failed(ResponseCode.USER_SESSION_PLEASE_LOGIN_FIRST);
        } else if (HttpStatus.BAD_REQUEST.value() == code) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        } else {
            return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
        }
    }
}

package com.im.admin.config;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 页面异常处理
 *
 * @author Barry
 * @date 2018/5/13
 */
@RestController
public class ServletExceptionHandler implements ErrorController {
    private ErrorAttributes errorAttributes;

    @Autowired
    public void setErrorAttributes(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    private static final Log LOG = LogFactory.get();

    @RequestMapping(
            value = {"/error"},
            produces = {"application/json;charset=UTF-8"}
    )
    @ResponseStatus(HttpStatus.OK)
    public Object errorHtml(HttpServletRequest request, HttpServletResponse response) {
        LOG.warn("无此接口：{}", RequestUtil.getRequestPath(request));
        return handleAjaxError(request, response);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    private RestResponse handleAjaxError(HttpServletRequest request, HttpServletResponse response) {
        int code = response.getStatus();
        if (HttpStatus.NOT_FOUND.value() == code) {
            return RestResponse.failed(ResponseCode.SYS_NO_SUCH_METHOD);
        } else if (HttpStatus.FORBIDDEN.value() == code) {
            return RestResponse.failed(ResponseCode.SYS_NO_PERMISSION);
        } else if (HttpStatus.UNAUTHORIZED.value() == code) {
            return RestResponse.failed(ResponseCode.SYS_NO_PERMISSION);
        } else if (HttpStatus.BAD_REQUEST.value() == code) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        } else {
            return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
        }
    }
}
package com.im.common.util.ip;

import cn.hutool.core.util.StrUtil;
import com.im.common.util.RequestUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 自动注入本次用户访问的IP
 *
 * @author Barry
 * @date 2019/2/14
 */
public class RequestIpResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 使用自定义的注解
        return parameter.hasParameterAnnotation(RequestIp.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String ip = RequestUtil.getRequestIp(request);
        RequestIp annotation = parameter.getParameterAnnotation(RequestIp.class);

        if (StrUtil.isBlank(ip)) {
            if (annotation.required()) {
                throw new IllegalArgumentException(StrUtil.isBlank(annotation.message()) ? "Can't recognize your ip" : annotation.message());
            }
        } else {
            return ip;
        }

        return ip;
    }
}
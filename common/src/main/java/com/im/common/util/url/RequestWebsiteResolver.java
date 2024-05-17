package com.im.common.util.url;

import cn.hutool.core.util.StrUtil;
import com.im.common.util.RequestUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 自动注入本次用户访问的域名
 *
 * @author Barry
 * @date 2019/2/14
 */
public class RequestWebsiteResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 使用自定义的注解
        return parameter.hasParameterAnnotation(RequestWebsite.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String website = RequestUtil.getRequestWebsite(request);
        RequestWebsite annotation = parameter.getParameterAnnotation(RequestWebsite.class);

        if (StrUtil.isBlank(website)) {
            if (annotation.required()) {
                throw new IllegalArgumentException(StrUtil.isBlank(annotation.message()) ? "Can't recognize your current website" : annotation.message());
            }
        } else {
            return website;
        }

        return website;
    }
}
package com.im.common.util.i18n;

import cn.hutool.core.util.StrUtil;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.GlobalConfigBO;
import com.im.common.constant.CommonConstant;
import com.im.common.util.RequestUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 国际化拦截器
 *
 * @author Barry
 * @date 2019-10-24
 */
public class I18nInterceptor implements HandlerInterceptor {
    private SysConfigCache sysConfigCache;

    public I18nInterceptor(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 设置国际化参数
        changeLanguage(request);
        return true;
    }

    private void changeLanguage(HttpServletRequest request) {
        // 优先在header中找
        String language = RequestUtil.getHeader(request, CommonConstant.LANGUAGE_HEADER);

        // 再在url中找
        if (StrUtil.isBlank(language)) {
            language = RequestUtil.getStringParamTrim(request, CommonConstant.LANGUAGE_HEADER);
        }

        // 没找到就用默认的
        if (StrUtil.isBlank(language)) {
            GlobalConfigBO bo = sysConfigCache.getGlobalConfigFromLocal();
            language = bo.getDefaultI18n();
        }

        // 设置
        I18nContext.setLanguage(language);
    }
}

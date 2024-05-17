package com.im.callback.config;

import com.im.common.cache.impl.SysConfigCache;
import com.im.common.util.i18n.I18nInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 * <p>
 * Spring执行顺序
 * Filter
 * 拦截器（手动指定order）
 * AOP（在AOP处理类指定@Order）
 *
 * @author Barry
 * @date 2019-10-12
 */
@Configuration
@ConditionalOnBean(DataCacheConfig.class)
public class InterceptorConfig implements WebMvcConfigurer {
    private SysConfigCache sysConfigCache;

    public InterceptorConfig(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Bean
    public I18nInterceptor getI18nInterceptor() {
        return new I18nInterceptor(sysConfigCache);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // I18N
        registry.addInterceptor(getI18nInterceptor())
                .order(1)
                .addPathPatterns("/**");
    }
}

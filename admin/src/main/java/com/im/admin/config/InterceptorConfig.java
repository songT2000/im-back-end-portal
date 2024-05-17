package com.im.admin.config;

import com.im.admin.controller.url.ApiUrl;
import com.im.common.cache.impl.AdminIpBlackWhiteCache;
import com.im.common.cache.impl.AdminRoleMenuCache;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.service.UserAuthTokenService;
import com.im.common.util.i18n.I18nInterceptor;
import com.im.common.util.redis.AdminRedisSessionManager;
import com.im.common.util.redis.SessionRefreshInterceptor;
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
@ConditionalOnBean({DataCacheConfig.class})
public class InterceptorConfig implements WebMvcConfigurer {
    private SysConfigCache sysConfigCache;
    private UserAuthTokenService userAuthTokenService;
    private AdminRedisSessionManager adminRedisSessionManager;
    private AdminRoleMenuCache adminRoleMenuCache;
    private AdminIpBlackWhiteCache adminIpBlackWhiteCache;

    public InterceptorConfig(SysConfigCache sysConfigCache,
                             UserAuthTokenService userAuthTokenService,
                             AdminRedisSessionManager adminRedisSessionManager,
                             AdminRoleMenuCache adminRoleMenuCache,
                             AdminIpBlackWhiteCache adminIpBlackWhiteCache) {
        this.sysConfigCache = sysConfigCache;
        this.userAuthTokenService = userAuthTokenService;
        this.adminRedisSessionManager = adminRedisSessionManager;
        this.adminRoleMenuCache = adminRoleMenuCache;
        this.adminIpBlackWhiteCache = adminIpBlackWhiteCache;
    }

    @Bean
    public I18nInterceptor getI18nInterceptor() {
        return new I18nInterceptor(sysConfigCache);
    }

    @Bean
    public AuthInterceptor getSessionInterceptor() {
        return new AuthInterceptor(userAuthTokenService, adminRoleMenuCache);
    }

    @Bean
    public SessionRefreshInterceptor getSessionRefreshInterceptor() {
        return new SessionRefreshInterceptor(adminRedisSessionManager);
    }

    @Bean
    public IpCheckerInterceptor getIpCheckerInterceptor() {
        return new IpCheckerInterceptor(adminIpBlackWhiteCache);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 会话延长刷新
        registry.addInterceptor(getSessionRefreshInterceptor())
                .order(0)
                .addPathPatterns(ApiUrl.BASE_NO_AUTH_URL + "/**");

        // I18N
        registry.addInterceptor(getI18nInterceptor())
                .order(1)
                .addPathPatterns("/**");

        // 所有以/api/auth/开头的接口都必须进行登录验证
        registry.addInterceptor(getSessionInterceptor())
                .order(2)
                .addPathPatterns(ApiUrl.BASE_AUTH_URL + "/**");

        // 每一次的操作IP检查
        registry.addInterceptor(getIpCheckerInterceptor())
                .order(3)
                .addPathPatterns(ApiUrl.BASE_NO_AUTH_URL + "/**");
    }
}

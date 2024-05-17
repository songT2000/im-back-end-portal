package com.im.portal.config;

import com.im.common.cache.impl.PortalAreaBlackWhiteCache;
import com.im.common.cache.impl.PortalIpBlackWhiteCache;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.service.UserAuthTokenService;
import com.im.common.util.i18n.I18nInterceptor;
import com.im.common.util.redis.PortalRedisSessionManager;
import com.im.common.util.redis.SessionRefreshInterceptor;
import com.im.portal.controller.url.ApiUrl;
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
    private UserAuthTokenService userAuthTokenService;
    private PortalRedisSessionManager portalRedisSessionManager;
    private PortalIpBlackWhiteCache portalIpBlackWhiteCache;
    private PortalAreaBlackWhiteCache portalAreaBlackWhiteCache;

    public InterceptorConfig(SysConfigCache sysConfigCache,
                             UserAuthTokenService userAuthTokenService,
                             PortalRedisSessionManager portalRedisSessionManager,
                             PortalIpBlackWhiteCache portalIpBlackWhiteCache,
                             PortalAreaBlackWhiteCache portalAreaBlackWhiteCache) {
        this.sysConfigCache = sysConfigCache;
        this.userAuthTokenService = userAuthTokenService;
        this.portalRedisSessionManager = portalRedisSessionManager;
        this.portalIpBlackWhiteCache = portalIpBlackWhiteCache;
        this.portalAreaBlackWhiteCache = portalAreaBlackWhiteCache;
    }

    @Bean
    public I18nInterceptor getI18nInterceptor() {
        return new I18nInterceptor(sysConfigCache);
    }

    /**
     * 必须写成getSessionInterceptor()，否则SessionInterceptor中的@Autowired会无效
     *
     * @return
     */
    @Bean
    public AuthInterceptor getSessionInterceptor() {
        return new AuthInterceptor(userAuthTokenService);
    }

    @Bean
    public SessionRefreshInterceptor getSessionRefreshInterceptor() {
        return new SessionRefreshInterceptor(portalRedisSessionManager);
    }

    @Bean
    public IpCheckerInterceptor getIpCheckerInterceptor() {
        return new IpCheckerInterceptor(portalIpBlackWhiteCache, portalAreaBlackWhiteCache, portalRedisSessionManager);
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
                .addPathPatterns("/**")
                .excludePathPatterns("/api/portal/recharge/common-callback/**", "/api/portal/withdraw/common-callback/**");
    }
}

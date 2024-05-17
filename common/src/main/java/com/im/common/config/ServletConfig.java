package com.im.common.config;

import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import java.util.Collections;

/**
 * Servlet配置
 *
 * @author Barry
 * @date 2018/5/26
 */
@Configuration
public class ServletConfig extends SpringBootServletInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
        SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
        sessionCookieConfig.setHttpOnly(true);
    }
}

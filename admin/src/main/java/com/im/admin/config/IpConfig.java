package com.im.admin.config;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.util.ip.IpAddressUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * 初始化IP数据库，根据IP获取地址位置
 *
 * @author Barry
 * @date 2018/5/13
 */
@Component
public class IpConfig implements InitializingBean, ApplicationContextAware {
    private static final Log LOG = LogFactory.get();

    /**
     * 当前上下文对象
     */
    private static ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        try {
            LOG.info("正在加载IP数据库");
            IpAddressUtil.init(getApplicationContext());
            LOG.info("完成加载IP数据库");
        } catch (Exception e) {
            LOG.error(e, "加载IP数据库失败！");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        IpConfig.applicationContext = applicationContext;
    }

    private static ApplicationContext getApplicationContext() {
        return IpConfig.applicationContext;
    }
}
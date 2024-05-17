package com.im.common.util.api.sms.base;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.enums.SmsChannelTypeEnum;
import com.im.common.util.ApplicationUtil;
import com.im.common.util.CollectionUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 短信信令通道工厂类
 *
 * @author Barry
 * @date 2022-02-10
 */
@Component
public class SmsHandlerFactory implements InitializingBean {
    private static final Log LOG = LogFactory.get();

    /**
     * 处理类
     **/
    public static Map<SmsChannelTypeEnum, SmsHandler> SMS_HANDLER_MAP = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            initHandlers();
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    /**
     * 获取处理类
     *
     * @param code 处理类编码
     * @param <T>  类型
     * @return 处理类
     */
    public static <T extends SmsHandler> T getSmsHandler(SmsChannelTypeEnum code) {
        return (T) SMS_HANDLER_MAP.get(code);
    }

    private static void initHandlers() throws InstantiationException, IllegalAccessException {
        Set<Class<?>> allSubclasses = ClassUtil.scanPackageBySuper(CommonConstant.PROJECT_BASE_PACKAGE + ".common.util.api.sms", SmsHandler.class);
        if (CollectionUtil.isEmpty(allSubclasses)) {
            SMS_HANDLER_MAP = new HashMap<>(4);
            return;
        }

        Map<SmsChannelTypeEnum, SmsHandler> handlerMapTmp = mapHandlers(allSubclasses);

        SMS_HANDLER_MAP = handlerMapTmp;
    }

    private static Map<SmsChannelTypeEnum, SmsHandler> mapHandlers(Collection<Class<?>> allSubclasses)
            throws IllegalAccessException, InstantiationException {
        Map<SmsChannelTypeEnum, SmsHandler> handlerMapTmp = new HashMap<>(allSubclasses.size());

        for (Class<?> subclass : allSubclasses) {
            SmsHandlerProperty annotation = subclass.getAnnotation(SmsHandlerProperty.class);
            if (annotation == null) {
                String errorMsg = StrUtil.format("必须为{}配置SmsHandlerProperty注解以表明该类是处理哪个三方短信信令通道的", subclass.getName());
                ApplicationUtil.printErrorWithExitSystem(true, errorMsg);
            }

            SmsChannelTypeEnum[] types = annotation.value();
            SmsHandler handler = (SmsHandler) subclass.newInstance();

            for (SmsChannelTypeEnum type : types) {
                if (handlerMapTmp.containsKey(type)) {
                    String errorMsg = StrUtil.format("重复三方短信信令通道处理器定义{},类{},请检查", type.getVal(), subclass.getName());
                    ApplicationUtil.printErrorWithExitSystem(true, errorMsg);
                }
                handlerMapTmp.put(type, handler);
            }
        }

        return handlerMapTmp;
    }
}

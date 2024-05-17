package com.im.common.util.api.pay.base.recharge;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.enums.ApiRechargeConfigCodeEnum;
import com.im.common.util.ApplicationUtil;
import com.im.common.util.CollectionUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 三方充值工厂类
 *
 * @author Barry
 * @date 2018/6/8
 */
@Component
public class ApiRechargeHandlerFactory implements InitializingBean {
    private static final Log LOG = LogFactory.get();

    /**
     * 充值处理类
     **/
    public static Map<ApiRechargeConfigCodeEnum, ApiRechargeHandler> RECHARGE_HANDLER_MAP = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            initHandlers();
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    /**
     * 获取充值处理类
     *
     * @param code 处理类编码
     * @param <T>  类型
     * @return 充值处理类
     */
    public static <T extends ApiRechargeHandler> T getRechargeHandler(ApiRechargeConfigCodeEnum code) {
        return (T) RECHARGE_HANDLER_MAP.get(code);
    }

    private static void initHandlers() throws InstantiationException, IllegalAccessException {
        Set<Class<?>> allSubclasses = ClassUtil.scanPackageBySuper(CommonConstant.PROJECT_BASE_PACKAGE + ".common.util.api.pay", ApiRechargeHandler.class);
        if (CollectionUtil.isEmpty(allSubclasses)) {
            RECHARGE_HANDLER_MAP = new HashMap<>(4);
            return;
        }

        Map<ApiRechargeConfigCodeEnum, ApiRechargeHandler> handlerMapTmp = mapHandlers(allSubclasses);

        RECHARGE_HANDLER_MAP = handlerMapTmp;
    }

    private static Map<ApiRechargeConfigCodeEnum, ApiRechargeHandler> mapHandlers(Collection<Class<?>> allSubclasses)
            throws IllegalAccessException, InstantiationException {
        Map<ApiRechargeConfigCodeEnum, ApiRechargeHandler> handlerMapTmp = new HashMap<>(allSubclasses.size());

        for (Class<?> subclass : allSubclasses) {
            ApiRechargeHandlerProperty annotation = subclass.getAnnotation(ApiRechargeHandlerProperty.class);
            if (annotation == null) {
                String errorMsg = StrUtil.format("必须为{}配置ApiRechargeHandlerProperty注解以表明该类是处理哪个三方充值渠道的", subclass.getName());
                ApplicationUtil.printErrorWithExitSystem(true, errorMsg);
            }

            ApiRechargeConfigCodeEnum[] codes = annotation.value();
            ApiRechargeHandler handler = (ApiRechargeHandler) subclass.newInstance();

            for (ApiRechargeConfigCodeEnum code : codes) {
                if (handlerMapTmp.containsKey(code)) {
                    String errorMsg = StrUtil.format("重复三方充值处理器定义{},类{},请检查", code.getVal(), subclass.getName());
                    ApplicationUtil.printErrorWithExitSystem(true, errorMsg);
                }
                handlerMapTmp.put(code, handler);
            }
        }

        return handlerMapTmp;
    }
}

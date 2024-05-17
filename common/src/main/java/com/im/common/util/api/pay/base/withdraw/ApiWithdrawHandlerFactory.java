package com.im.common.util.api.pay.base.withdraw;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.enums.ApiWithdrawConfigCodeEnum;
import com.im.common.util.ApplicationUtil;
import com.im.common.util.CollectionUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * API代付工厂类
 *
 * @author Barry
 * @date 2018/6/8
 */
@Component
public class ApiWithdrawHandlerFactory implements InitializingBean {
    private static final Log LOG = LogFactory.get();

    /**
     * 充值处理类
     **/
    public static Map<ApiWithdrawConfigCodeEnum, ApiWithdrawHandler> WITHDRAW_HANDLER_MAP = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            initHandlers();
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    /**
     * 获取代付处理类
     *
     * @param code 处理类编码
     * @param <T>  类型
     * @return 充值处理类
     */
    public static <T extends ApiWithdrawHandler> T getWithdrawHandler(ApiWithdrawConfigCodeEnum code) {
        return (T) WITHDRAW_HANDLER_MAP.get(code);
    }

    private static void initHandlers() throws InstantiationException, IllegalAccessException {
        Set<Class<?>> allSubclasses = ClassUtil.scanPackageBySuper(CommonConstant.PROJECT_BASE_PACKAGE + ".common.util.api.pay", ApiWithdrawHandler.class);
        if (CollectionUtil.isEmpty(allSubclasses)) {
            WITHDRAW_HANDLER_MAP = new HashMap<>(4);
            return;
        }

        Map<ApiWithdrawConfigCodeEnum, ApiWithdrawHandler> handlerMapTmp = mapHandlers(allSubclasses);

        WITHDRAW_HANDLER_MAP = handlerMapTmp;
    }

    private static Map<ApiWithdrawConfigCodeEnum, ApiWithdrawHandler> mapHandlers(Collection<Class<?>> allSubclasses)
            throws IllegalAccessException, InstantiationException {
        Map<ApiWithdrawConfigCodeEnum, ApiWithdrawHandler> handlerMapTmp = new HashMap<>(allSubclasses.size());

        for (Class<?> subclass : allSubclasses) {
            ApiWithdrawHandlerProperty annotation = subclass.getAnnotation(ApiWithdrawHandlerProperty.class);
            if (annotation == null) {
                String errorMsg = StrUtil.format("必须为{}配置ApiWithdrawHandlerProperty注解以表明该类是处理哪个API代付渠道的", subclass.getName());
                ApplicationUtil.printErrorWithExitSystem(true, errorMsg);
            }

            ApiWithdrawConfigCodeEnum[] codes = annotation.value();
            ApiWithdrawHandler handler = (ApiWithdrawHandler) subclass.newInstance();

            for (ApiWithdrawConfigCodeEnum code : codes) {
                if (handlerMapTmp.containsKey(code)) {
                    String errorMsg = StrUtil.format("重复API代付处理器定义{},类{},请检查", code.getVal(), subclass.getName());
                    ApplicationUtil.printErrorWithExitSystem(true, errorMsg);
                }
                handlerMapTmp.put(code, handler);
            }
        }

        return handlerMapTmp;
    }
}

package com.im.common.util.spring;

import cn.hutool.core.util.StrUtil;
import com.im.common.constant.CommonConstant;
import com.im.common.util.CollectionUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Spring 上下文，可在XML中声明，可在java中声明，必须先调用setApplicationContext
 *
 * @author Barry
 * @date 2018/5/12
 */
public class SpringContextUtil {
    private static ApplicationContext APPLICATION_CONTEXT;

    /**
     * 设置spring上下文，使用工具类前必须先调用该方法来设置上下文对象
     *
     * @param applicationContext spring上下文
     * @throws BeansException
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        APPLICATION_CONTEXT = applicationContext;
    }

    /**
     * 获取容器
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    /**
     * 获取 HttpServletRequest
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }

    /**
     * 获取 HttpServletRequest
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 获取容器对象
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> type) {
        if (APPLICATION_CONTEXT == null) {
            return null;
        }
        return APPLICATION_CONTEXT.getBean(type);
    }

    /**
     * 判断当前环境是否包含dev环境，即spring.active.profiles=dev
     *
     * @return boolean
     */
    public static boolean isInDevEnvironment() {
        return isInProfile(CommonConstant.PROFILE_DEV);
    }

    /**
     * 判断当前环境是否包含test环境，即spring.active.profiles=test
     *
     * @return boolean
     */
    public static boolean isInTestEnvironment() {
        return isInProfile(CommonConstant.PROFILE_TEST);
    }

    /**
     * 判断当前环境是否包含pro环境，即spring.active.profiles=pro
     *
     * @return boolean
     */
    public static boolean isInProEnvironment() {
        return isInProfile(CommonConstant.PROFILE_PRO);
    }

    /**
     * 判断当前环境是否是指定的profile，即spring.active.profiles
     *
     * @return boolean
     */
    public static boolean isInProfile(String profile) {
        if (StrUtil.isBlank(profile)) {
            return false;
        }

        String[] activeProfiles = APPLICATION_CONTEXT.getEnvironment().getActiveProfiles();
        if (CollectionUtil.isEmpty(activeProfiles)) {
            return false;
        }

        for (String activeProfile : activeProfiles) {
            if (profile.equals(activeProfile)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断当前环境是否是指定的任意一个profile，即spring.active.profiles
     *
     * @return boolean
     */
    public static boolean isInAnyProfiles(String... profiles) {
        if (CollectionUtil.isEmpty(profiles)) {
            return false;
        }

        String[] activeProfiles = APPLICATION_CONTEXT.getEnvironment().getActiveProfiles();
        if (CollectionUtil.isEmpty(activeProfiles)) {
            return false;
        }

        Set<String> argumentProfileSet = CollectionUtil.toSet(profiles);

        for (String activeProfile : activeProfiles) {
            if (argumentProfileSet.contains(activeProfile)) {
                return true;
            }
        }

        return false;
    }
}
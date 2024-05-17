package com.im.common.cache.sysconfig;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.entity.enums.SysConfigGroupEnum;
import com.im.common.util.ApplicationUtil;
import com.im.common.util.CollectionUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 系统配置解析器工厂类,必须先调用init方法,然后再调用getResolver
 *
 * @author Barry
 * @date 2018/6/8
 */
class SysConfigResolverFactory {
    private static final Log LOG = LogFactory.get();

    /**
     * 缓存
     **/
    private static Map<SysConfigGroupEnum, SysConfigResolver> RESOLVER_MAP = null;

    /**
     * 是否已经初始化
     **/
    private static boolean INITIALLED = false;

    /**
     * 初始化所有解析器,只需要调用一次即可
     */
    static void initResolvers() {
        try {
            cacheResolvers();
            INITIALLED = true;
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    /**
     * 根据类型获取解析器
     *
     * @param group 类型
     * @return 解析器
     */
    static <T extends SysConfigResolver> T getResolver(SysConfigGroupEnum group) {
        if (INITIALLED == false) {
            throw new IllegalStateException("请先调用init方法初始化");

        }
        return (T) RESOLVER_MAP.get(group);
    }

    /**
     * 将SysConfigResolver同目录或子目录的所有子类配置解析器缓存起来
     *
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private static void cacheResolvers() throws InstantiationException, IllegalAccessException {
        Set<Class<?>> allSubclasses = ClassUtil.scanPackageBySuper("com.im.common.cache.sysconfig", SysConfigResolver.class);
        if (CollectionUtil.isEmpty(allSubclasses)) {
            RESOLVER_MAP = new HashMap<>(4);
            return;
        }

        Map<SysConfigGroupEnum, SysConfigResolver> resolverMapTmp = mapResolvers(allSubclasses);

        RESOLVER_MAP = resolverMapTmp;
    }

    private static Map<SysConfigGroupEnum, SysConfigResolver> mapResolvers(Collection<Class<?>> allSubclasses) throws IllegalAccessException, InstantiationException {
        Map<SysConfigGroupEnum, SysConfigResolver> resolverMapTmp = new HashMap<>(allSubclasses.size());

        for (Class<?> subclass : allSubclasses) {
            SysConfigResolverGroup annotation = subclass.getAnnotation(SysConfigResolverGroup.class);
            if (annotation == null) {
                String errorMsg = StrUtil.format("必须为{}配置SysConfigResolverGroup注解以表明该类是处理哪个系统配置组的", subclass.getName());
                ApplicationUtil.printErrorWithExitSystem(true, errorMsg);
            }

            SysConfigGroupEnum group = annotation.value();

            if (resolverMapTmp.containsKey(group)) {
                String errorMsg = StrUtil.format("重复系统配置解析器定义{},类{},请检查", group.getVal(), subclass.getName());
                ApplicationUtil.printErrorWithExitSystem(true, errorMsg);
            }

            SysConfigResolver resolver = (SysConfigResolver) subclass.newInstance();

            LOG.info("初始化[SYS_CONFIG]解析处理类：{}={}", group.getVal(), resolver.getClass().getName());

            resolverMapTmp.put(group, resolver);
        }

        return resolverMapTmp;
    }
}

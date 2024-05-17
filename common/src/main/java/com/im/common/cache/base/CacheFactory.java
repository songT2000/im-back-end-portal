package com.im.common.cache.base;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.util.ApplicationUtil;
import com.im.common.util.ClassUtil;
import com.im.common.util.spring.SpringContextUtil;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存工具类工厂类，外部声明该对象到Spring里即可使用
 *
 * @author Barry
 * @date 2018/6/8
 */
public class CacheFactory {
    private static final Log LOG = LogFactory.get();

    /**
     * 缓存处理类
     **/
    private static Map<SysCacheRefreshTypeEnum, BaseCacheHandler> CACHE_MAP = new HashMap<>();

    private ApplicationContext applicationContext;

    public CacheFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取缓存处理器
     *
     * @param typeEnum 类型
     * @param <T>      调用方注单转换返回值类型
     * @return 缓存处理器
     */
    public <T extends BaseCacheHandler> T getCacheHandler(SysCacheRefreshTypeEnum typeEnum) {
        BaseCacheHandler cacheHandler = CACHE_MAP.get(typeEnum);
        return (T) CACHE_MAP.get(typeEnum);

        // BaseCacheHandler cacheHandler = CACHE_MAP.get(typeEnum);
        // if (cacheHandler != null) {
        //     return (T) cacheHandler;
        // }
        //
        // initCacheHandlers();
        //
        // return (T) CACHE_MAP.get(typeEnum);
    }

    /**
     * 获取所有缓存处理器
     *
     * @return 所有缓存处理器
     */
    public Map<SysCacheRefreshTypeEnum, BaseCacheHandler> getCacheHandlers() {
        // initCacheHandlers();

        return CACHE_MAP;
    }


    /**
     * 初始化方法
     */
    public void init() {
        // 初始化所有缓存处理类
        // initCacheHandlers();
        checkHandlers();
    }

    public static void putHandler(SysCacheRefreshTypeEnum type, BaseCacheHandler handler) {
        // 必须是spring对象
        BaseCacheHandler bean = SpringContextUtil.getBean(handler.getClass());

        if (bean != null) {
            CACHE_MAP.put(type, handler);
        }
    }

    /**
     * 初始化所有缓存处理类,并分组
     */
    private void initCacheHandlers() {
        // 结果集
        Map<SysCacheRefreshTypeEnum, BaseCacheHandler> cacheMapTmp = new HashMap<>(16);

        // 所有子类,必须是spring对象
        // 这里不能正确拿到所有已声明的缓存spring处理类，但是没关系，后面getCacheHandler那边和getCacheHandlers，都会重新调用该方法
        Map<String, BaseCacheHandler> beansOfType = this.applicationContext.getBeansOfType(BaseCacheHandler.class);

        beansOfType.forEach((className, baseCache) -> {
            Class<? extends BaseCacheHandler> cacheClass = baseCache.getClass();
            CacheProperty cacheProperty = cacheClass.getAnnotation(CacheProperty.class);
            if (cacheProperty == null) {
                String errorMsg = StrUtil.format("必须为{}配置CacheProperty注解以表明该类是处理哪个缓存的", className);
                ApplicationUtil.printError(errorMsg);
                throw new IllegalStateException(errorMsg);
            }

            SysCacheRefreshTypeEnum type = cacheProperty.value();

            if (cacheMapTmp.containsKey(type)) {
                String errorMsg = StrUtil.format("重复缓存定义{},类{},请检查", type.getVal(), className);
                ApplicationUtil.printError(errorMsg);
                throw new IllegalStateException(errorMsg);
            }

            cacheMapTmp.put(type, baseCache);
        });

        // 缓存
        CACHE_MAP = cacheMapTmp;
    }

    private void checkHandlers() {
        // 结果集
        Map<SysCacheRefreshTypeEnum, Class> handlerMap = new HashMap<>(16);

        List<Class<?>> handlerClassList = ClassUtil.getAllSubClasses(CommonConstant.PROJECT_BASE_PACKAGE + ".common.cache.impl",
                BaseCacheHandler.class);

        handlerClassList.forEach((handlerClass) -> {
            CacheProperty cacheProperty = handlerClass.getAnnotation(CacheProperty.class);
            ApplicationUtil
                    .printErrorWithExitSystem(cacheProperty == null,
                            StrUtil.format("必须为{}配置CacheProperty注解以表明该类是处理哪个缓存的", handlerClass.getName()));

            SysCacheRefreshTypeEnum type = cacheProperty.value();
            ApplicationUtil
                    .printErrorWithExitSystem(handlerMap.containsKey(type),
                            StrUtil.format("重复缓存定义{},类{},请检查", type.getVal(), handlerClass.getName()));

            handlerMap.put(type, handlerClass);
        });
    }
}

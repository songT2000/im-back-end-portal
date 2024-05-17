package com.im.common.cache.sysconfig;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.cache.sysconfig.bo.BaseSysConfigBO;
import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.SysConfigGroupEnum;
import com.im.common.util.CollectionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 系统配置解析代理类
 *
 * @author Barry
 * @date 2018/6/8
 */
public class SysConfigResolverProxy {
    private static final Log LOG = LogFactory.get();

    /**
     * 初始化所有解析器,只需要在系统启动时调用一次即可
     */
    public static void initResolvers() {
        SysConfigResolverFactory.initResolvers();
    }

    /**
     * 将所有配置解析并按照类型进行分组
     *
     * @param sysConfigs 所有配置项列表
     * @return HashMap
     */
    public static Map<SysConfigGroupEnum, BaseSysConfigBO> resolve(List<SysConfig> sysConfigs) {
        // 返回解析后的结果集
        Map<SysConfigGroupEnum, BaseSysConfigBO> groupMap = new HashMap<>(SysConfigGroupEnum.values().length);

        // 将sysConfigs分组
        Map<SysConfigGroupEnum, List<SysConfig>> groupEnumListMap = CollectionUtil.toMapList(sysConfigs,
                sysConfig -> sysConfig.getGroup() != null,
                SysConfig::getGroup);

        // 按组来找到resolver并解析
        if (groupEnumListMap != null) {
            groupEnumListMap.forEach((group, groupSysConfigs) -> {
                BaseSysConfigBO groupData = resolve(group, groupSysConfigs);
                Optional.ofNullable(groupData).ifPresent(presentData -> groupMap.put(group, presentData));
            });
        }

        return groupMap;
    }

    /**
     * 将一组已经分好组的配置解析成为一个单一对象
     *
     * @param group           配置项
     * @param groupSysConfigs 配置项列表
     * @return 配置对象, 根据需要转换成子类
     */
    public static BaseSysConfigBO resolve(SysConfigGroupEnum group, List<SysConfig> groupSysConfigs) {
        SysConfigResolver resolver = SysConfigResolverFactory.getResolver(group);
        if (resolver == null) {
            LOG.warn("没有为系统配置{}找到解析器，请检查", group.getVal());
            return null;
        }

        return resolver.resolve(groupSysConfigs);
    }

    /**
     * 创建一个空的配置
     *
     * @param group 配置项
     * @return 配置对象, 根据需要转换成子类
     */
    public static BaseSysConfigBO getDefault(SysConfigGroupEnum group) {
        SysConfigResolver resolver = SysConfigResolverFactory.getResolver(group);
        if (resolver == null) {
            LOG.warn("没有为系统配置{}找到解析器，请检查", group.getStr());
            return null;
        }

        return resolver.getDefault();
    }
}

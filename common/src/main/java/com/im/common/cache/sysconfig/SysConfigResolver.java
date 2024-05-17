package com.im.common.cache.sysconfig;

import com.im.common.cache.sysconfig.bo.BaseSysConfigBO;
import com.im.common.entity.SysConfig;

import java.util.List;

/**
 * 系统配置解析接口
 *
 * @author Barry
 * @date 2018/6/8
 */
public interface SysConfigResolver<T extends BaseSysConfigBO> {
    /**
     * 将一组已经分好组的配置解析成为一个单一对象
     *
     * @param sysConfigs 一组已经分好组的配置
     * @return 转换好的对象
     */
    T resolve(List<SysConfig> sysConfigs);

    /**
     * 创建一个空的配置项,并全部采用默认值
     *
     * @return 空的配置项
     */
    T getDefault();
}

package com.im.common.config;

import com.im.common.util.fastjson.FastJsonConfigUtil;

/**
 * FastJson配置
 *
 * @author Barry
 * @date 2019/2/18
 */
public class FastJsonConfig {
    static {
        // 系统刚启动时就配置
        FastJsonConfigUtil.configFastJson();
    }
}
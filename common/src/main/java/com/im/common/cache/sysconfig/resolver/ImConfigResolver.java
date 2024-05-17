package com.im.common.cache.sysconfig.resolver;

import cn.hutool.core.convert.Convert;
import com.im.common.cache.sysconfig.SysConfigResolver;
import com.im.common.cache.sysconfig.SysConfigResolverGroup;
import com.im.common.cache.sysconfig.bo.ImConfigBO;
import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.SysConfigGroupEnum;
import com.im.common.util.StrUtil;

import java.util.List;

/**
 * IM配置解析
 *
 * @author Barry
 * @date 2021-02-20
 */
@SysConfigResolverGroup(SysConfigGroupEnum.IM)
public class ImConfigResolver implements SysConfigResolver<ImConfigBO> {
    @Override
    public ImConfigBO resolve(List<SysConfig> sysConfigs) {
        ImConfigBO config = new ImConfigBO();

        sysConfigs.forEach(sysConfig -> this.resolveSingle(config, sysConfig));

        // 每次初始化都重新签名
        config.renewSig();

        return config;
    }

    @Override
    public ImConfigBO getDefault() {
        return new ImConfigBO();
    }

    private void resolveSingle(ImConfigBO config, SysConfig sysConfig) {
        String item = sysConfig.getItem();
        String value = sysConfig.getValue();

        switch (item) {
            case "TECENT_IM_SDK_URL":
                config.setTecentImSdkUrl(StrUtil.trim(value));
                break;
            case "TECENT_IM_SDK_APPID":
                config.setTecentImSdkAppid(Convert.toLong(value, 0L));
                break;
            case "TECENT_IM_SDK_KEY":
                config.setTecentImSdkKey(StrUtil.trim(value));
                break;
            case "TECENT_IM_SDK_IDENTIFIER":
                config.setTecentImSdkIdentifier(StrUtil.trim(value));
                break;
            default:
                break;
        }
    }
}

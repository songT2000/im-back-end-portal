package com.im.common.cache.sysconfig.resolver;

import cn.hutool.core.convert.Convert;
import com.im.common.cache.sysconfig.SysConfigResolver;
import com.im.common.cache.sysconfig.SysConfigResolverGroup;
import com.im.common.cache.sysconfig.bo.RedEnvelopeConfigBO;
import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.SysConfigGroupEnum;
import com.im.common.util.StrUtil;

import java.util.List;

/**
 * 红包配置解析
 *
 * @author Barry
 * @date 2021-01-18
 */
@SysConfigResolverGroup(SysConfigGroupEnum.RED_ENVELOPE)
public class RedEnvelopeConfigResolver implements SysConfigResolver<RedEnvelopeConfigBO> {
    @Override
    public RedEnvelopeConfigBO resolve(List<SysConfig> sysConfigs) {
        RedEnvelopeConfigBO config = new RedEnvelopeConfigBO();

        sysConfigs.forEach(sysConfig -> this.resolveSingle(config, sysConfig));

        return config;
    }

    @Override
    public RedEnvelopeConfigBO getDefault() {
        return new RedEnvelopeConfigBO();
    }

    private void resolveSingle(RedEnvelopeConfigBO config, SysConfig sysConfig) {
        String item = sysConfig.getItem();
        String value = sysConfig.getValue();

        switch (item) {
            case "EXPIRE_HOURS":
                config.setExpireHours(Convert.toInt(value, 24));
                break;
            case "PERSONAL_AMOUNT_RANGE":
                config.setPersonalAmountRange(StrUtil.trim(value));
                break;
            case "GROUP_AVERAGE_AMOUNT_RANGE":
                config.setGroupAverageAmountRange(StrUtil.trim(value));
                break;
            case "GROUP_MAX_NUM":
                config.setGroupMaxNum(Convert.toInt(value, 100));
                break;
            default:
                break;
        }
    }
}

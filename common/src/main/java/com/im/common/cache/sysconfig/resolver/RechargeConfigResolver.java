package com.im.common.cache.sysconfig.resolver;

import cn.hutool.core.convert.Convert;
import com.im.common.cache.sysconfig.SysConfigResolver;
import com.im.common.cache.sysconfig.SysConfigResolverGroup;
import com.im.common.cache.sysconfig.bo.RechargeConfigBO;
import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.SysConfigGroupEnum;

import java.util.List;

/**
 * 充值配置解析
 *
 * @author Barry
 * @date 2021-02-20
 */
@SysConfigResolverGroup(SysConfigGroupEnum.RECHARGE)
public class RechargeConfigResolver implements SysConfigResolver<RechargeConfigBO> {
    @Override
    public RechargeConfigBO resolve(List<SysConfig> sysConfigs) {
        RechargeConfigBO config = new RechargeConfigBO();

        sysConfigs.forEach(sysConfig -> this.resolveSingle(config, sysConfig));

        return config;
    }

    @Override
    public RechargeConfigBO getDefault() {
        return new RechargeConfigBO();
    }

    private void resolveSingle(RechargeConfigBO config, SysConfig sysConfig) {
        String item = sysConfig.getItem();
        String value = sysConfig.getValue();

        switch (item) {
            case "ENABLED":
                config.setEnabled(Convert.toBool(value, false));
                break;
            case "FUND_PWD_BIND_REQUIRED":
                config.setFundPwdBindRequired(Convert.toBool(value, false));
                break;
            case "BANK_CARD_BIND_REQUIRED":
                config.setBankCardBindRequired(Convert.toBool(value, false));
                break;
            default:
                break;
        }
    }
}

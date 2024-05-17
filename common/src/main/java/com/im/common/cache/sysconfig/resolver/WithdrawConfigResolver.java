package com.im.common.cache.sysconfig.resolver;

import cn.hutool.core.convert.Convert;
import com.im.common.cache.sysconfig.SysConfigResolver;
import com.im.common.cache.sysconfig.SysConfigResolverGroup;
import com.im.common.cache.sysconfig.bo.WithdrawConfigBO;
import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.SysConfigGroupEnum;
import com.im.common.util.StrUtil;

import java.util.List;

/**
 * 提现配置解析
 *
 * @author Barry
 * @date 2021-02-20
 */
@SysConfigResolverGroup(SysConfigGroupEnum.WITHDRAW)
public class WithdrawConfigResolver implements SysConfigResolver<WithdrawConfigBO> {
    @Override
    public WithdrawConfigBO resolve(List<SysConfig> sysConfigs) {
        WithdrawConfigBO config = new WithdrawConfigBO();

        sysConfigs.forEach(sysConfig -> this.resolveSingle(config, sysConfig));

        return config;
    }

    @Override
    public WithdrawConfigBO getDefault() {
        return new WithdrawConfigBO();
    }

    private void resolveSingle(WithdrawConfigBO config, SysConfig sysConfig) {
        String item = sysConfig.getItem();
        String value = sysConfig.getValue();

        switch (item) {
            case "ENABLED":
                config.setEnabled(Convert.toBool(value, false));
                break;
            case "ENABLE_TIME":
                config.setEnableTime(StrUtil.trim(value));
                break;
            case "MAX_DAILY_REQUEST":
                config.setMaxDailyRequest(Convert.toInt(value, 0));
                break;
            case "MAX_SAME_TIME_REQUEST":
                config.setMaxSameTimeRequest(Convert.toInt(value, 0));
                break;
            case "NEED_APPROVE":
                config.setNeedApprove(Convert.toBool(value, true));
                break;
            case "MANUAL_PAY_DIRECT_SUCCESS":
                config.setManualPayDirectSuccess(Convert.toBool(value, true));
                break;
            case "MAX_BIND_BANK_CARD_COUNT":
                config.setMaxBindBankCardCount(Convert.toInt(value, 20));
                break;
            default:
                break;
        }
    }
}

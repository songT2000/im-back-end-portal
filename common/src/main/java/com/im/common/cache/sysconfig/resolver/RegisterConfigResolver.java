package com.im.common.cache.sysconfig.resolver;

import cn.hutool.core.convert.Convert;
import com.im.common.cache.sysconfig.SysConfigResolver;
import com.im.common.cache.sysconfig.SysConfigResolverGroup;
import com.im.common.cache.sysconfig.bo.RegisterConfigBO;
import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.SysConfigGroupEnum;

import java.util.List;

/**
 * 注册配置解析
 *
 * @author Barry
 * @date 2021-02-20
 */
@SysConfigResolverGroup(SysConfigGroupEnum.REGISTER)
public class RegisterConfigResolver implements SysConfigResolver<RegisterConfigBO> {
    @Override
    public RegisterConfigBO resolve(List<SysConfig> sysConfigs) {
        RegisterConfigBO config = new RegisterConfigBO();

        sysConfigs.forEach(sysConfig -> this.resolveSingle(config, sysConfig));

        return config;
    }

    @Override
    public RegisterConfigBO getDefault() {
        return new RegisterConfigBO();
    }

    private void resolveSingle(RegisterConfigBO config, SysConfig sysConfig) {
        String item = sysConfig.getItem();
        String value = sysConfig.getValue();

        switch (item) {
            case "ENABLED":
                config.setEnabled(Convert.toBool(value, false));
                break;
            case "HOME_ENABLED":
                config.setHomeEnabled(Convert.toBool(value, false));
                break;
            case "INVITE_CODE_REQUIRED":
                config.setInviteCodeRequired(Convert.toBool(value, false));
                break;
            case "MOBILE_REQUIRED":
                config.setMobileRequired(Convert.toBool(value, false));
                break;
            case "MOBILE_VERIFICATION_CODE_REQUIRED":
                config.setMobileVerificationCodeRequired(Convert.toBool(value, false));
                break;
            case "FUND_PWD_REQUIRED":
                config.setFundPwdRequired(Convert.toBool(value, false));
                break;
            case "DAILY_LIMIT_COUNT_BY_IP":
                config.setDailyLimitCountByIp(Convert.toInt(value, 10));
                break;
            case "AUTO_GENERATE_INVITE_CODE_LENGTH":
                config.setAutoGenerateInviteCodeLength(Convert.toInt(value, 8));
                break;
            case "ADD_FRIEND_ENABLED":
                config.setAddFriendEnabled(Convert.toBool(value, true));
                break;
            case "AVATAR_REQUIRED":
                config.setAvatarRequired(Convert.toBool(value, false));
                break;
            default:
                break;
        }
    }
}

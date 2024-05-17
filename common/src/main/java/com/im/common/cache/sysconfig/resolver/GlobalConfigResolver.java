package com.im.common.cache.sysconfig.resolver;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.im.common.cache.sysconfig.SysConfigResolver;
import com.im.common.cache.sysconfig.SysConfigResolverGroup;
import com.im.common.cache.sysconfig.bo.GlobalConfigBO;
import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.SysConfigGroupEnum;

import java.util.List;

/**
 * 全局配置解析
 *
 * @author Barry
 * @date 2018/6/8
 */
@SysConfigResolverGroup(SysConfigGroupEnum.GLOBAL)
public class GlobalConfigResolver implements SysConfigResolver<GlobalConfigBO> {
    @Override
    public GlobalConfigBO resolve(List<SysConfig> sysConfigs) {
        GlobalConfigBO globalConfig = new GlobalConfigBO();

        sysConfigs.forEach(sysConfig -> this.resolveSingle(globalConfig, sysConfig));

        return globalConfig;
    }

    @Override
    public GlobalConfigBO getDefault() {
        return new GlobalConfigBO();
    }

    private void resolveSingle(GlobalConfigBO globalConfig, SysConfig sysConfig) {
        String item = sysConfig.getItem();
        String value = sysConfig.getValue();

        switch (item) {
            case "GOOGLE_ENABLED":
                globalConfig.setGoogleEnabled(Convert.toBool(value, false));
                break;
            case "GOOGLE_ISSUER":
                globalConfig.setGoogleIssuer(StrUtil.isBlank(value) ? "" : StrUtil.trim(value));
                break;
            case "JWT_SECRET":
                globalConfig.setJwtSecret(StrUtil.trim(value));
                break;
            case "DEFAULT_I18N":
                globalConfig.setDefaultI18n(StrUtil.trim(value));
                break;
            case "API_CALLBACK_URL":
                globalConfig.setApiCallbackUrl(StrUtil.trim(value));
                break;
            case "PORTAL_ADMIN_ACCOUNT":
                globalConfig.setPortalAdminAccount(StrUtil.trim(value));
                break;
            default:
                break;
        }
    }
}

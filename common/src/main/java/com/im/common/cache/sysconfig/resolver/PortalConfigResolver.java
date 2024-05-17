package com.im.common.cache.sysconfig.resolver;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.im.common.cache.sysconfig.SysConfigResolver;
import com.im.common.cache.sysconfig.SysConfigResolverGroup;
import com.im.common.cache.sysconfig.bo.PortalConfigBO;
import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.SysConfigGroupEnum;

import java.util.List;

/**
 * 前台配置解析
 *
 * @author Barry
 * @date 2021-01-18
 */
@SysConfigResolverGroup(SysConfigGroupEnum.PORTAL)
public class PortalConfigResolver implements SysConfigResolver<PortalConfigBO> {
    @Override
    public PortalConfigBO resolve(List<SysConfig> sysConfigs) {
        PortalConfigBO config = new PortalConfigBO();

        sysConfigs.forEach(sysConfig -> this.resolveSingle(config, sysConfig));

        return config;
    }

    @Override
    public PortalConfigBO getDefault() {
        return new PortalConfigBO();
    }

    private void resolveSingle(PortalConfigBO config, SysConfig sysConfig) {
        String item = sysConfig.getItem();
        String value = sysConfig.getValue();

        switch (item) {
            case "ICON_URL":
                config.setIconUrl(StrUtil.isBlank(value) ? "https://minio.ew119.com/tim-file/image/logo.png" : StrUtil.trim(value));
                break;
            case "SYSTEM_NAME":
                config.setSystemName(StrUtil.isBlank(value) ? "IM" : StrUtil.trim(value));
                break;
            case "LOGIN_EXPIRE_MINUTES":
                config.setLoginExpireMinutes(Convert.toLong(value, 30L));
                break;
            case "ALLOW_WEB_MOBILE_SAME_LOGIN":
                config.setAllowWebMobileSameLogin(Convert.toBool(value, false));
                break;
            case "WEB_LOGIN_MAX_CLIENT":
                config.setWebLoginMaxClient(Convert.toInt(value, 1));
                break;
            case "MOBILE_LOGIN_MAX_CLIENT":
                config.setMobileLoginMaxClient(Convert.toInt(value, 1));
                break;
            default:
                break;
        }
    }
}

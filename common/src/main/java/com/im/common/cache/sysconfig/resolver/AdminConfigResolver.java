package com.im.common.cache.sysconfig.resolver;

import cn.hutool.core.convert.Convert;
import com.im.common.cache.sysconfig.SysConfigResolver;
import com.im.common.cache.sysconfig.SysConfigResolverGroup;
import com.im.common.cache.sysconfig.bo.AdminConfigBO;
import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.SysConfigGroupEnum;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;

import java.util.List;

/**
 * 后台配置解析
 *
 * @author Barry
 * @date 2018/6/8
 */
@SysConfigResolverGroup(SysConfigGroupEnum.ADMIN)
public class AdminConfigResolver implements SysConfigResolver<AdminConfigBO> {
    @Override
    public AdminConfigBO resolve(List<SysConfig> sysConfigs) {
        AdminConfigBO adminConfig = new AdminConfigBO();

        sysConfigs.forEach(sysConfig -> this.resolveSingle(adminConfig, sysConfig));

        return adminConfig;
    }

    @Override
    public AdminConfigBO getDefault() {
        return new AdminConfigBO();
    }

    private void resolveSingle(AdminConfigBO adminConfig, SysConfig sysConfig) {
        String item = sysConfig.getItem();
        String value = sysConfig.getValue();

        switch (item) {
            case "NAME":
                adminConfig.setName(StrUtil.trim(value));
                break;
            case "LOGO":
                adminConfig.setLogo(StrUtil.trim(value));
                break;
            case "FAVICON":
                adminConfig.setFavicon(StrUtil.trim(value));
                break;
            case "LOGIN_PWD_ERROR_TIMES":
                adminConfig.setLoginPwdErrorTimes(Convert.toInt(value, 10));
                break;
            case "LOGIN_EXPIRE_MINUTES":
                adminConfig.setLoginExpireMinutes(Convert.toLong(value, 30L));
                break;
            default:
                break;
        }
    }
}

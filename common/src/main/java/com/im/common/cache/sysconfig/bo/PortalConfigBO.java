package com.im.common.cache.sysconfig.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前台配置
 *
 * @author Barry
 * @date 2021-01-18
 */
@Data
@NoArgsConstructor
public class PortalConfigBO extends BaseSysConfigBO {
    /**
     * 免登录分钟，一次登录，后续多久时间内无需要再次登录，30~99999999
     **/
    private Long loginExpireMinutes;

    /**
     * 是否允许WEB/手机同时登录，如果允许，则IM修改为双平台登录，如果不允许，则IM修改为单平台登录
     **/
    private Boolean allowWebMobileSameLogin;

    /**
     * WEB端最大允许同时在线，需要在IM设置【Web 端可同时在线个数】一样的数量，小于等于0表示不限制
     **/
    private Integer webLoginMaxClient;

    /**
     * 手机端最大允许同时在线，需要在IM设置【Android、iPhone、iPad、Windows、Mac、Linux 平台，每种平台可同时在线设备个数】一样的数量，小于等于0表示不限制v
     **/
    private Integer mobileLoginMaxClient;

    /**
     * 显示在app和web首页应用的名称，比如“酷聊”，“速聊”
     */
    private String systemName;
    /**
     * 显示在app和web首页应用的icon地址
     */
    private String iconUrl;
}

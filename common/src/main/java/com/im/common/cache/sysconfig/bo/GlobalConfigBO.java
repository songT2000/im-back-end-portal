package com.im.common.cache.sysconfig.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全局配置
 * <p>
 * 请注意，如果在这里新增字段，必须在GlobalConfigResolver#getDefault()中加入相应的默认值
 *
 * @author Barry
 * @date 2018/6/8
 */
@Data
@NoArgsConstructor
public class GlobalConfigBO extends BaseSysConfigBO {
    /**
     * 谷歌身份验证器是否启用，所有系统同时生效
     **/
    private Boolean googleEnabled;

    /**
     * 显示在谷歌身份验证器里的标识，为空则不显示
     */
    private String googleIssuer;

    /**
     * JWT的密钥，该密钥不可在接口中传送出去
     **/
    private String jwtSecret;

    /**
     * 系统会优先寻找用户浏览器的语言进行匹配，如未正确匹配以支持的语言，优先以该语言进行显示
     */
    private String defaultI18n;

    /**
     * API回调域名，所有涉及三方回调的API域名
     */
    private String apiCallbackUrl;

    /**
     * 前台管理员账号，用于接收一些系统通知消息
     */
    private String portalAdminAccount;
}

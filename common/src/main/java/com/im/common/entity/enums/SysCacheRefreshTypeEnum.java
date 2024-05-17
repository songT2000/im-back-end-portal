package com.im.common.entity.enums;

/**
 * <p>系统配置->配置组，需要实现BaseEnum接口</p>
 *
 * <p>对应sys_cache_refresh.type</p>
 *
 * @author Barry
 * @date 2018/6/8
 */
public enum SysCacheRefreshTypeEnum implements BaseEnum {
    /**
     * 系统配置
     **/
    SYS_CONFIG("SYS_CONFIG", "系统配置"),

    /**
     * 后台管理用户
     **/
    ADMIN_USER("ADMIN_USER", "后台管理用户"),

    /**
     * 菜单&后台管理用户权限
     **/
    ADMIN_MENU("ADMIN_MENU", "后台菜单&后台管理用户权限"),

    /**
     * 角色列表
     **/
    ADMIN_ROLE("ADMIN_ROLE", "后台角色列表"),

    /**
     * 角色菜单列表
     **/
    ADMIN_ROLE_MENU("ADMIN_ROLE_MENU", "后台角色菜单列表"),

    /**
     * 后台IP黑白名单
     */
    ADMIN_IP_BLACK_WHITE("ADMIN_IP_BLACK_WHITE", "后台IP黑白名单"),

    /**
     * 国际化语言类型配置
     **/
    I18N_LANGUAGE("I18N_LANGUAGE", "国际化语言类型配置"),

    /**
     * 国际化翻译表
     **/
    I18N_TRANSLATE("I18N_TRANSLATE", "国际化翻译表"),

    /**
     * 前台用户
     */
    PORTAL_USER("PORTAL_USER", "前台用户"),

    /**
     * 用户区域黑白名单
     */
    PORTAL_AREA_BLACK_WHITE("PORTAL_AREA_BLACK_WHITE", "用户区域黑白名单"),

    /**
     * 用户IP黑白名单
     */
    PORTAL_IP_BLACK_WHITE("PORTAL_IP_BLACK_WHITE", "用户IP黑白名单"),
    /**
     * 系统公告
     */
    SYS_NOTICE("SYS_NOTICE", "系统公告"),

    /**
     * 系统测速线路
     */
    SYS_CIRCUIT("SYS_CIRCUIT", "系统测速线路"),
    /**
     * 好友关系
     */
    TIM_FRIEND("TIM_FRIEND", "好友关系"),

    /**
     * 银行
     */
    BANK("BANK", "银行"),

    /**
     * 用户卡黑名单
     */
    USER_BANK_CARD_BLACK("USER_BANK_CARD_BLACK", "用户卡黑名单"),

    /**
     * 银行卡充值配置
     */
    BANK_CARD_RECHARGE_CONFIG("BANK_CARD_RECHARGE_CONFIG", "银行卡充值配置"),

    /**
     * 银行卡提现配置
     */
    BANK_CARD_WITHDRAW_CONFIG("BANK_CARD_WITHDRAW_CONFIG", "银行卡提现配置"),

    /**
     * 三方充值配置
     */
    API_RECHARGE_CONFIG("API_RECHARGE_CONFIG", "三方充值配置"),

    /**
     * API代付配置
     */
    API_WITHDRAW_CONFIG("API_WITHDRAW_CONFIG", "API代付配置"),

    /**
     * 群组消息同步缓存
     */
    GROUP_MESSAGE_SYNC("GROUP_MESSAGE_SYNC", "群组消息同步缓存"),

    /**
     * 群组缓存
     */
    TIM_GROUP("TIM_GROUP", "群组缓存"),
    /**
     * 短信模板配置
     */
    SMS_TEMPLATE_CONFIG("SMS_TEMPLATE_CONFIG", "短信模板配置"),
    /**
     * 短信信令通道配置
     */
    SMS_CHANNEL_CONFIG("SMS_CHANNEL_CONFIG", "短信信令通道配置"),
    /**
     * 应用版本缓存
     */
    APP_VERSION("APP_VERSION", "应用版本缓存"),

    /**
     * 用户组
     */
    USER_GROUP("USER_GROUP", "用户组"),
    USER_GROUP_USER("USER_GROUP_USER", "用户组内用户"),
    USER_GROUP_BANK_CARD_RECHARGE_CONFIG("USER_GROUP_BANK_CARD_RECHARGE_CONFIG", "组银行卡充值配置"),
    USER_GROUP_API_RECHARGE_CONFIG("USER_GROUP_API_RECHARGE_CONFIG", "组三方充值配置"),

    /**
     * 前台导航
     */
    PORTAL_NAVIGATOR("PORTAL_NAVIGATOR", "前台导航"),

    /**
     * 表情包
     */
    TIM_FACE("TIM_FACE", "表情包"),

    /**
     * 用户自定义表情
     */
    PORTAL_USER_EMOJI("PORTAL_USER_EMOJI", "用户自定义表情"),

    /**
     * 敏感词
     */
    SENSITIVE_WORD("SENSITIVE_WORD", "敏感词"),

    /**
     * 自动回复配置
     */
    APP_AUTO_REPLY_CONFIG("APP_AUTO_REPLY_CONFIG", "自动回复配置"),

    ;

    private String val;
    private String str;

    SysCacheRefreshTypeEnum(String val, String str) {
        this.val = val;
        this.str = str;
    }

    @Override
    public String getVal() {
        return val;
    }

    @Override
    public String getStr() {
        return str;
    }

    @Override
    public String toString() {
        return this.val;
    }
}

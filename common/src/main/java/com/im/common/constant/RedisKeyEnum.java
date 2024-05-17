package com.im.common.constant;


import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 定义系统所有Redis Key，所有的Redis Key尽量定义在这里，方便统一管理
 *
 * @author Barry
 * @date 2019-10-10
 */
public enum RedisKeyEnum implements IEnum<String> {
    /**
     * sys_cache_refresh表
     **/
    SYS_CACHE_REFRESH("SYS:CACHE:REFRESH"),
    SYS_CONFIG("SYS:CONFIG"),
    SYS_REQUEST_LIMIT_BY_IP("SYS:REQUEST:LIMIT_BY_IP:{}:{}"),
    SYS_REQUEST_LIMIT_BY_USER("SYS:REQUEST:LIMIT_BY_USER:{}:{}"),
    SYS_REQUEST_LIMIT_BY_GLOBAL("SYS:REQUEST:LIMIT_BY_GLOBAL:{}"),
    SYS_REQUEST_LIMIT_BY_REQUEST_BODY_KEY("SYS:REQUEST:LIMIT_BY_REQUEST_BODY_KEY:{}:{}"),
    ADMIN_ROLE_MENU_ID("ADMIN_ROLE_MENU:ID:{}"),
    ADMIN_ROLE_MENU_CODE("ADMIN_ROLE_MENU:CODE:{}"),
    ADMIN_ROLE_MENU_URL("ADMIN_ROLE_MENU:URL:{}"),
    PORTAL_USER_SESSION("SESSION:PORTAL"),
    ADMIN_USER_SESSION("SESSION:ADMIN"),
    USER_ACTION_LOG("USER:ACTION_LOG"),
    I18N_LANGUAGE_BY_CODE("I18N_LANGUAGE:BY_CODE"),
    SYS_NOTICE("SYS_NOTICE:{}"),
    USER_MOMENTS("USER_MOMENTS:{}"),
    TIM_FRIEND("TIM_FRIEND:{}"),
    SYS_BANNER("SYS_BANNER:{}"),
    SYS_CIRCUIT("SYS_CIRCUIT"),
    BANK("BANK"),
    APP_VERSION("APP:VERSION"),
    VC("VC"),
    /**
     * 货币详情，VC_MARKET_DETAIL，hash，key是vc.code
     */
    VC_MARKET_DETAIL("VC_MARKET_DETAIL"),
    /**
     * 货币K线，VC_MARKET_DETAIL:BTC:1M
     */
    VC_MARKET_KLINE("VC_MARKET_KLINE:{}:{}"),
    /**
     * 货币结算详情K线，VC_MARKET_DETAIL_KLINE_CALC:BTC
     */
    VC_MARKET_DETAIL_KLINE_CALC("VC_MARKET_DETAIL_KLINE_CALC:{}"),
    /**
     * 货币1分钟结算K线，VC_MARKET_ONE_MINUTE_KLINE_CALC:BTC
     */
    VC_MARKET_ONE_MINUTE_KLINE_CALC("VC_MARKET_ONE_MINUTE_KLINE_CALC:{}"),
    /**
     * 链名称
     */
    VC_CHAIN_NAME("VC_CHAIN_NAME"),
    /**
     * 充值配置
     */
    VC_RECHARGE_CONFIG("VC_RECHARGE_CONFIG"),
    /**
     * 提现配置
     */
    VC_WITHDRAW_CONFIG("VC_WITHDRAW_CONFIG"),
    /**
     * 兑换配置
     */
    VC_EXCHANGE_CONFIG("VC_EXCHANGE_CONFIG"),
    /**
     * 极速交易配置
     */
    VC_TIMING_TRADE_CONFIG("VC_TIMING_TRADE_CONFIG"),

    /**
     * 极速交易规则配置
     */
    VC_TIMING_TRADE_RULE_CONFIG("VC_TIMING_TRADE_RULE_CONFIG"),
    /**
     * 合约交易配置
     */
    CONTRACT_TRADE_CONFIG("CONTRACT_TRADE_CONFIG"),
    /**
     * 合约交易杠杆配置
     */
    CONTRACT_TRADE_CONFIG_LEVERAGE("CONTRACT_TRADE_CONFIG_LEVERAGE"),

    /**
     * 注册-同IP注册数量
     */
    REGISTER_DAILY_LIMIT_COUNT_BY_IP("REGISTER:DAILY_LIMIT_COUNT_BY_IP:{}:{}"),
    /**
     * 极速交易注单
     */
    MQTT_TIMING_TRADE_ORDER("MQTT:TIMING_TRADE_ORDER"),

    /**
     * 银行卡充值配置
     */
    BANK_CARD_RECHARGE_CONFIG("BANK_CARD_RECHARGE_CONFIG"),

    /**
     * 三方充值配置
     */
    API_RECHARGE_CONFIG("API_RECHARGE_CONFIG"),

    /**
     * API代付配置
     */
    API_WITHDRAW_CONFIG("API_WITHDRAW_CONFIG"),

    /**
     * 银行卡提现配置
     */
    BANK_CARD_WITHDRAW_CONFIG("BANK_CARD_WITHDRAW_CONFIG"),

    /**
     * 合约订单计算缓存，hash
     */
    CONTRACT_TRADE_CALC_BASE("CONTRACT_TRADE_CALC_BASE"),
    CONTRACT_TRADE_CALC_WIN_LOSS("CONTRACT_TRADE_CALC_STOP_LOSS_WIN"),
    /**
     * 合约订单平仓缓存，hash
     */
    CONTRACT_TRADE_CALC_STOP("CONTRACT_TRADE_CALC_STOP"),
    /**
     * 腾讯IM每日连接用户数据
     */
    TIM_USER_CONNECTION("TIM:USER:CONNECTION"),

    /**
     * 短信验证码，SMS:VERIFICATION_BY_MOBILE:{国家编码}:{手机号}
     */
    SMS_VERIFICATION_BY_MOBILE("SMS:VERIFICATION_BY_MOBILE:{}:{}"),

    /**
     * 短信验证码，SMS:VERIFICATION_BY_IP:{IP}
     */
    SMS_VERIFICATION_BY_IP("SMS:VERIFICATION_BY_MOBILE:{}"),

    /**
     * 发现模块
     */
    PORTAL_NAVIGATOR("PORTAL_NAVIGATOR"),
    /**
     * 表情包
     */
    TIM_FACE("TIM_FACE"),
    /**
     * 用户自定义表情包
     */
    PORTAL_USER_EMOJI("PORTAL_USER_EMOJI_"),

    /**
     * 敏感词
     */
    SENSITIVE_WORD("SENSITIVE_WORD"),

    /**
     * 迁移数据：导入账号进度和成功的数据集合
     */
    SWITCH_APP_IMPORT_ACCOUNT("SWITCH_APP:IMPORT_ACCOUNT"),
    SWITCH_APP_IMPORT_ACCOUNT_SUCCESS("SWITCH_APP:IMPORT_ACCOUNT_SUCCESS"),

    /**
     * 迁移数据：导入账号资料进度和成功的数据集合
     */
    SWITCH_APP_IMPORT_ACCOUNT_PORTRAIT("SWITCH_APP:IMPORT_ACCOUNT_PORTRAIT"),
    SWITCH_APP_IMPORT_ACCOUNT_PORTRAIT_SUCCESS("SWITCH_APP:IMPORT_ACCOUNT_PORTRAIT_SUCCESS"),

    /**
     * 迁移数据：导入好友数据进度和成功的数据集合
     */
    SWITCH_APP_IMPORT_FRIEND("SWITCH_APP:IMPORT_FRIEND"),
    SWITCH_APP_IMPORT_FRIEND_SUCCESS("SWITCH_APP:IMPORT_FRIEND_SUCCESS"),
    /**
     * 迁移数据：导入单聊记录进度和成功的数据集合
     */
    SWITCH_APP_IMPORT_C2C_MESSAGE("SWITCH_APP:IMPORT_C2C_MESSAGE"),
    SWITCH_APP_IMPORT_C2C_MESSAGE_SUCCESS("SWITCH_APP:IMPORT_C2C_MESSAGE_SUCCESS"),

    /**
     * 迁移数据：导入群组进度和成功的数据集合
     */
    SWITCH_APP_IMPORT_GROUP("SWITCH_APP:IMPORT_GROUP"),
    SWITCH_APP_IMPORT_GROUP_SUCCESS("SWITCH_APP:IMPORT_GROUP_SUCCESS"),

    /**
     * 迁移数据：导入群组成员进度和成功的数据集合
     */
    SWITCH_APP_IMPORT_GROUP_MEMBER("SWITCH_APP:IMPORT_GROUP_MEMBER"),
    SWITCH_APP_IMPORT_GROUP_MEMBER_SUCCESS("SWITCH_APP:IMPORT_GROUP_MEMBER_SUCCESS"),

    /**
     * 迁移数据：导入群组成员进度和成功的数据集合
     */
    SWITCH_APP_IMPORT_GROUP_MESSAGE("SWITCH_APP:IMPORT_GROUP_MESSAGE"),
    SWITCH_APP_IMPORT_GROUP_MESSAGE_SUCCESS("SWITCH_APP:IMPORT_GROUP_MESSAGE_SUCCESS"),

    /**
     * 自动回复配置
     */
    APP_AUTO_REPLY_CONFIG("APP_AUTO_REPLY_CONFIG"),
    ;

    private String val;

    RedisKeyEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    @Override
    public String getValue() {
        return this.val;
    }

    @Override
    public String toString() {
        return this.val;
    }
}

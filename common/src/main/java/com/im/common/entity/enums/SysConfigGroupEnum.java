package com.im.common.entity.enums;

/**
 * 系统配置->配置组，需要实现BaseEnum接口
 * <p>
 * 这个数据对应sys_config表里面的group
 *
 * @author Barry
 * @date 2018/6/8
 */
public enum SysConfigGroupEnum implements BaseEnum {
    /**
     * 全局配置
     **/
    GLOBAL("GLOBAL", "全局配置"),

    /**
     * 管理后台配置
     **/
    ADMIN("ADMIN", "后台配置"),

    /**
     * 前台配置
     **/
    PORTAL("PORTAL", "前台配置"),

    /**
     * 注册配置
     **/
    REGISTER("REGISTER", "注册配置"),

    /**
     * 充值配置
     **/
    RECHARGE("RECHARGE", "充值配置"),

    /**
     * 提现配置
     **/
    WITHDRAW("WITHDRAW", "提现配置"),

    /**
     * 报表配置
     **/
    REPORT("REPORT", "报表配置"),

    /**
     * OSS配置
     **/
    OSS("OSS", "OSS配置"),

    /**
     * MQTT
     **/
    MQTT("MQTT", "MQTT"),

    /**
     * IM
     **/
    IM("IM", "IM"),

    /**
     * RED_ENVELOPE
     **/
    RED_ENVELOPE("RED_ENVELOPE", "红包配置");

    private String val;
    private String str;

    SysConfigGroupEnum(String val, String str) {
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

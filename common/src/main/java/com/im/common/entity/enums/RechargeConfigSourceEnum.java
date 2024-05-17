package com.im.common.entity.enums;

/**
 * 充值配置来源
 *
 * @author Barry
 * @date 2021-03-20
 */
public enum RechargeConfigSourceEnum implements BaseEnum {
    /**
     * 银行卡充值配置
     **/
    BANK_CARD_RECHARGE_CONFIG("1", "银行卡充值配置"),
    /**
     * 三方充值配置
     **/
    API_RECHARGE_CONFIG("2", "三方充值配置");

    private String val;
    private String str;

    RechargeConfigSourceEnum(String val, String str) {
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

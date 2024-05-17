package com.im.common.entity.enums;

/**
 * 充值配置分组，只是用来做前台展示归类
 *
 * @author Barry
 * @date 2021-03-20
 */
public enum RechargeConfigGroupEnum implements BaseEnum {
    /**
     * 银行卡
     **/
    BANK_CARD("1", "银行卡"),
    /**
     * 支付宝
     */
    ALIPAY("2", "支付宝"),
    /**
     * 微信
     */
    WECHAT("3", "微信");

    private String val;
    private String str;

    RechargeConfigGroupEnum(String val, String str) {
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

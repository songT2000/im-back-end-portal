package com.im.common.entity.enums;

/**
 * 个人红包状态
 *
 * @author Barry
 * @date 2021-04-20
 */
public enum PersonalRedEnvelopeStatusEnum implements BaseEnum {
    /**
     * 未领取
     **/
    UN_RECEIVED("1", "未领取"),
    /**
     * 已领取
     **/
    RECEIVED("2", "已领取"),
    /**
     * 已退回
     **/
    REFUND("3", "已退回"),
    /**
     * 已过期
     **/
    EXPIRED("4", "已过期");

    private String val;
    private String str;

    PersonalRedEnvelopeStatusEnum(String val, String str) {
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

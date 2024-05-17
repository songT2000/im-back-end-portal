package com.im.common.entity.enums;

/**
 * 群红包状态
 *
 * @author Barry
 * @date 2021-04-20
 */
public enum GroupRedEnvelopeStatusEnum implements BaseEnum {
    /**
     * 未领取
     **/
    UN_RECEIVED("1", "未领取"),
    /**
     * 部分领取
     **/
    PART_RECEIVED("2", "部分领取"),
    /**
     * 全部领取
     **/
    ALL_RECEIVED("3", "全部领取"),
    /**
     * 已过期
     **/
    EXPIRED("4", "已过期");

    private String val;
    private String str;

    GroupRedEnvelopeStatusEnum(String val, String str) {
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

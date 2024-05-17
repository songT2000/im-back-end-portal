package com.im.common.entity.enums;

/**
 * 充值订单状态
 *
 * @author Barry
 * @date 2020-06-22
 */
public enum RechargeOrderStatusEnum implements BaseEnum {
    /**
     * 待支付
     **/
    WAITING("1", "待支付"),
    /**
     * 已完成
     */
    FINISHED("2", "已完成"),
    /**
     * 已失败
     */
    FAILED("3", "已失败");

    private String val;
    private String str;

    RechargeOrderStatusEnum(String val, String str) {
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

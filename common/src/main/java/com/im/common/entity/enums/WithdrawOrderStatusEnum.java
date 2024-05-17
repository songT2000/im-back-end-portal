package com.im.common.entity.enums;

/**
 * 提现订单状态
 *
 * @author Barry
 * @date 2021-03-26
 */
public enum WithdrawOrderStatusEnum implements BaseEnum {
    /**
     * 待处理
     **/
    WAITING("1", "待处理"),
    /**
     * 审核通过
     **/
    APPROVE_SUCCESS("2", "审核通过"),
    /**
     * 审核拒绝
     **/
    APPROVE_DENY("3", "审核拒绝"),
    /**
     * 打款中
     **/
    PAYING("4", "打款中"),
    /**
     * 打款拒绝
     **/
    PAY_DENY("5", "打款拒绝"),
    /**
     * 打款完成
     **/
    PAY_SUCCESS("6", "打款完成"),
    /**
     * 打款失败
     **/
    PAY_FAILED("7", "打款失败");

    private String val;
    private String str;

    WithdrawOrderStatusEnum(String val, String str) {
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

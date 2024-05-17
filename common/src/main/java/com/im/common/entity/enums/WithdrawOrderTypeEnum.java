package com.im.common.entity.enums;

/**
 * 提现方式
 *
 * @author Barry
 * @date 2020-06-22
 */
public enum WithdrawOrderTypeEnum implements BaseEnum {
    /**
     * 用户提现
     **/
    USER_REQUEST("1", "用户提现"),
    /**
     * 人工提现
     */
    ADMIN_ADD("2", "人工提现");

    private String val;
    private String str;

    WithdrawOrderTypeEnum(String val, String str) {
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

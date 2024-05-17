package com.im.common.entity.enums;

/**
 * 打款方式
 *
 * @author Barry
 * @date 2021-03-26
 */
public enum WithdrawOrderPayTypeEnum implements BaseEnum {
    /**
     * 手动打款
     **/
    MANUAL_PAY("1", "手动打款"),
    /**
     * API代付
     */
    API_PAY("2", "API代付");

    private String val;
    private String str;

    WithdrawOrderPayTypeEnum(String val, String str) {
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

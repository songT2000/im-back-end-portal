package com.im.common.entity.enums;

/**
 * 提现配置来源
 *
 * @author Barry
 * @date 2021-03-20
 */
public enum WithdrawConfigSourceEnum implements BaseEnum {
    /**
     * 银行卡提现配置
     **/
    BANK_CARD_WITHDRAW_CONFIG("1", "银行卡提现配置");

    private String val;
    private String str;

    WithdrawConfigSourceEnum(String val, String str) {
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

package com.im.common.entity.enums;

/**
 * 用户账变类型
 *
 * @author Barry
 * @date 2020-05-23
 */
public enum UserBillTypeEnum implements BaseEnum {
    /**
     * 管理员增减余额
     **/
    ADMIN_ADD_BALANCE("ADMIN_ADD_BALANCE", "管理员增减"),

    USER_RECHARGE("USER_RECHARGE", "用户充值"),
    ADMIN_RECHARGE("ADMIN_RECHARGE", "人工充值"),
    ADMIN_RECHARGE_GIVE("ADMIN_RECHARGE_GIVE", "人工充值赠送"),

    USER_WITHDRAW("USER_WITHDRAW", "用户提现"),
    USER_WITHDRAW_FEE_AMOUNT("USER_WITHDRAW_FEE_AMOUNT", "用户提现手续费"),
    USER_WITHDRAW_FALLBACK("USER_WITHDRAW_FALLBACK", "用户提现退回"),
    USER_WITHDRAW_FEE_AMOUNT_FALLBACK("USER_WITHDRAW_FEE_AMOUNT_FALLBACK", "用户提现手续费退回"),
    ADMIN_WITHDRAW("ADMIN_WITHDRAW", "人工提现"),
    ADMIN_WITHDRAW_FEE_AMOUNT("ADMIN_WITHDRAW_FEE_AMOUNT", "人工提现手续费"),

    PERSONAL_RED_ENVELOPE_SEND("PERSONAL_RED_ENVELOPE_SEND", "发个人红包"),
    PERSONAL_RED_ENVELOPE_RECEIVE("PERSONAL_RED_ENVELOPE_RECEIVE", "收个人红包"),
    PERSONAL_RED_ENVELOPE_EXPIRE("PERSONAL_RED_ENVELOPE_EXPIRE", "个人红包过期"),
    PERSONAL_RED_ENVELOPE_REFUND("PERSONAL_RED_ENVELOPE_REFUND", "退回个人红包"),

    GROUP_RED_ENVELOPE_SEND("GROUP_RED_ENVELOPE_SEND", "发群红包"),
    GROUP_RED_ENVELOPE_RECEIVE("GROUP_RED_ENVELOPE_RECEIVE", "收群红包"),
    GROUP_RED_ENVELOPE_EXPIRE("GROUP_RED_ENVELOPE_EXPIRE", "群红包过期");

    private String val;
    private String str;

    UserBillTypeEnum(String val, String str) {
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

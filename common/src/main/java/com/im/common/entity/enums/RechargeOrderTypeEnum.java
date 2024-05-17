package com.im.common.entity.enums;

/**
 * 充值方式
 *
 * @author Barry
 * @date 2020-06-22
 */
public enum RechargeOrderTypeEnum implements BaseEnum {
    /**
     * 用户充值
     **/
    USER_REQUEST("1", "用户充值"),
    /**
     * 管理员增减余额
     */
    ADMIN_ADD_BALANCE("2", "管理员增减余额"),
    /**
     * 人工充值
     */
    ADMIN_ADD("3", "人工充值"),
    /**
     * 人工充值赠送
     */
    ADMIN_ADD_GIVE("4", "人工充值赠送");

    private String val;
    private String str;

    RechargeOrderTypeEnum(String val, String str) {
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

package com.im.common.entity.enums;

/**
 * 充值订单完成方式
 *
 * @author Barry
 * @date 2020-06-22
 */
public enum RechargeOrderFinishTypeEnum implements BaseEnum {
    /**
     * 管理员补单
     **/
    ADMIN_PATCHED("1", "管理员补单"),
    API_CALLBACK("2", "三方回调"),
    AUTOMATIC_SYNC("3", "自动同步");

    private String val;
    private String str;

    RechargeOrderFinishTypeEnum(String val, String str) {
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

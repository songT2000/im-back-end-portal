package com.im.common.entity.enums;

/**
 * 用户查询条件排序类型
 *
 * @author Barry
 * @date 2020-07-08
 */
public enum PortalUserOrderByTypeEnum implements BaseEnum {
    /**
     * 创建时间
     **/
    BALANCE_DESC("1", "按余额降序"),
    BALANCE_ASC("2", "按余额升序"),
    CREATE_TIME_DESC("3", "按注册时间降序"),
    CREATE_TIME_ASC("4", "按注册时间升序"),
    TOTAL_RECHARGE_AMOUNT_DESC("5", "按累充金额降序"),
    TOTAL_RECHARGE_AMOUNT_ASC("6", "按累充金额升序"),
    TOTAL_WITHDRAW_AMOUNT_DESC("7", "按累提金额降序"),
    TOTAL_WITHDRAW_AMOUNT_ASC("8", "按累提金额升序");

    private String val;
    private String str;

    PortalUserOrderByTypeEnum(String val, String str) {
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

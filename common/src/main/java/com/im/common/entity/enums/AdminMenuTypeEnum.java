package com.im.common.entity.enums;

/**
 * 系统菜单和权限表 -> 类型
 *
 * @author Barry
 * @date 2019-11-06
 */
public enum AdminMenuTypeEnum implements BaseEnum {
    /**
     * 菜单
     **/
    MENU("1", "菜单"),

    /**
     * 按钮
     **/
    BUTTON("2", "按钮");

    private String val;
    private String str;

    AdminMenuTypeEnum(String val, String str) {
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

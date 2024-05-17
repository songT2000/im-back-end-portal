package com.im.common.entity.enums;

/**
 * 门户类型
 *
 * @author Barry
 * @date 2020-05-23
 */
public enum PortalTypeEnum implements BaseEnum {
    /**
     * 前台
     **/
    PORTAL("1", "前台"),

    /**
     * 后台
     **/
    ADMIN("2", "后台");

    private String val;
    private String str;

    PortalTypeEnum(String val, String str) {
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

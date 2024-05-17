package com.im.common.entity.enums;

/**
 * 开启/关闭
 */
public enum OnOrOffEnum implements BaseEnum {
    /**
     * 开启
     **/
    ON("On", "开启"),
    /**
     * Off
     **/
    OFF("Off", "关闭");

    private String val;
    private String str;

    OnOrOffEnum(String val, String str) {
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

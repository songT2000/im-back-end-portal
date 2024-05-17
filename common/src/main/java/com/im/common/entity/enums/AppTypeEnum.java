package com.im.common.entity.enums;

/**
 * 应用类型
 */
public enum AppTypeEnum implements BaseEnum {
    /**
     * android
     **/
    android("android", "安卓"),

    /**
     * iOS
     **/
    iOS("iOS", "苹果");

    private String val;
    private String str;

    AppTypeEnum(String val, String str) {
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

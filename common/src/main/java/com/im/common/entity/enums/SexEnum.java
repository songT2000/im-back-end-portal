package com.im.common.entity.enums;

/**
 * 性别
 *
 * @author Barry
 * @date 2021-04-20
 */
public enum SexEnum implements BaseEnum {
    /**
     * 男
     **/
    MALE("1", "男"),
    /**
     * 女
     **/
    FEMALE("2", "女");

    private String val;
    private String str;

    SexEnum(String val, String str) {
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

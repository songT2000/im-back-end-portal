package com.im.common.entity.enums;

/**
 * 证件类型
 *
 * @author Max
 * @date 2021-08-11
 */
public enum IdCardTypeEnum implements BaseEnum {
    /**
     * 身份证
     **/
    ID_CARD("1", "身份证"),

    /**
     * 护照
     **/
    PASSPORT("2", "护照"),

    /**
     * 驾照
     **/
    DRIVER_LICENSE("3", "驾照");

    private String val;
    private String str;

    IdCardTypeEnum(String val, String str) {
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

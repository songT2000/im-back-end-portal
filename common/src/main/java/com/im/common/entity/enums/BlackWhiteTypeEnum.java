package com.im.common.entity.enums;

/**
 * 黑白名单
 *
 * @author Barry
 * @date 2021-04-20
 */
public enum BlackWhiteTypeEnum implements BaseEnum {
    /**
     * 黑名单
     **/
    BLACK("1", "黑名单"),
    /**
     * 白名单
     **/
    WHITE("2", "白名单");

    private String val;
    private String str;

    BlackWhiteTypeEnum(String val, String str) {
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

package com.im.common.entity.enums;

/**
 * 朋友圈动态类型
 *
 * @author Max
 */
public enum TrendsTypeEnum implements BaseEnum {
    /**
     * 所有好友
     **/
    ALL_FRIEND("1", "公开"),

    /**
     * 仅自己可见
     **/
    ONLY_ME("2", "仅自己可见"),

    /**
     * 部分可见
     **/
    ALLOW_FRIEND("3", "部分可见"),
    /**
     * 不准谁看
     */
    REJECT_FRIEND("4", "不给谁看");

    private String val;
    private String str;

    TrendsTypeEnum(String val, String str) {
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

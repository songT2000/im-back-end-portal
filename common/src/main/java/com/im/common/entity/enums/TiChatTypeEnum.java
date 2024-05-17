package com.im.common.entity.enums;

/**
 * 图片类型
 */
public enum TiChatTypeEnum implements BaseEnum {
    C2C("C2C", "单聊"),
    Group("Group", "群聊"),
    ;

    private String val;
    private String str;

    TiChatTypeEnum(String val, String str) {
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

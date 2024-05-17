package com.im.common.entity.enums;


/**
 * 聊天类型
 */
public enum ChatTypeEnum implements BaseEnum{
    C2C("C2C","单聊"),
    Group("Group","群聊"),
    ;
    ChatTypeEnum(String val, String str) {
        this.val = val;
        this.str = str;
    }

    private String val;
    private String str;


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

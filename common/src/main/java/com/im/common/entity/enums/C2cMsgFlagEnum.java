package com.im.common.entity.enums;

/**
 * 单聊消息状态
 */
public enum C2cMsgFlagEnum implements BaseEnum{
    /**
     * 正常消息
     **/
    NORMAL("0", "正常消息"),
    /**
     * 已撤回消息
     **/
    WITHDRAW("8", "已撤回消息");

    private String val;
    private String str;

    C2cMsgFlagEnum(String val, String str) {
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

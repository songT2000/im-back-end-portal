package com.im.common.entity.enums;

/**
 * 群组类型
 */
public enum GroupTypeEnum implements BaseEnum {
    Public("Public","陌生人社交群"),
    Private("Private","好友工作群"),
    ChatRoom("ChatRoom","会议群"),
    Community("Community","社群"),
    ;
    GroupTypeEnum(String val, String str) {
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

package com.im.common.entity.enums;


/**
 * 关系链字段
 */
public enum TiFriendItemEnum implements BaseEnum{
    Group("Tag_SNS_IM_Group","好友分组"),
    Remark("Tag_SNS_IM_Remark","好友备注"),
    AddWording("Tag_SNS_IM_AddWording","加好友附言"),
    AddSource("Tag_SNS_IM_AddSource","加好友来源"),
    AddTime("Tag_SNS_IM_AddTime","加好友的时间戳"),
    ;
    TiFriendItemEnum(String val, String str) {
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

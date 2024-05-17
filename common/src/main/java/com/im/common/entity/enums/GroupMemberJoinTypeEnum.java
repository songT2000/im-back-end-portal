package com.im.common.entity.enums;

/**
 * 进群方式
 */
public enum GroupMemberJoinTypeEnum implements BaseEnum {
    Apply("Apply", "申请入群"),
    Invited("Invited", "邀请入群"),
    ;

    private String val;
    private String str;

    GroupMemberJoinTypeEnum(String val, String str) {
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

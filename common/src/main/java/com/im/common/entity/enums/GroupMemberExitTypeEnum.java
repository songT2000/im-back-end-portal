package com.im.common.entity.enums;

/**
 * 退群方式
 */
public enum GroupMemberExitTypeEnum implements BaseEnum {
    Kicked("Kicked", "被踢"),
    Quit("Quit", "主动退群"),
    ;

    private String val;
    private String str;

    GroupMemberExitTypeEnum(String val, String str) {
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

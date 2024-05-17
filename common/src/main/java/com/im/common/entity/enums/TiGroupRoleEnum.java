package com.im.common.entity.enums;

/**
 * 群组角色类型
 */
public enum TiGroupRoleEnum implements BaseEnum {
    Admin("Admin", "群管理员"),
    Member("Member", "普通成员"),
    ;

    private String val;
    private String str;

    TiGroupRoleEnum(String val, String str) {
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

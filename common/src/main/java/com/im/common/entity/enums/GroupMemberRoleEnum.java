package com.im.common.entity.enums;

/**
 * 群内成员角色
 */
public enum GroupMemberRoleEnum implements BaseEnum {
    /**
     * 群主
     **/
    Owner("Owner", "群主"),

    /**
     * 管理员
     **/
    Admin("Admin", "管理员"),

    /**
     * 普通成员
     **/
    Member("Member", "普通成员"),

    /**
     * 非群成员
     */
    NotMember("NotMember","非群成员"),
    ;

    private String val;
    private String str;

    GroupMemberRoleEnum(String val, String str) {
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

    public static GroupMemberRoleEnum getByVal(String value){
        GroupMemberRoleEnum anEnum = null;
        for (GroupMemberRoleEnum item : GroupMemberRoleEnum.values()) {
            if(item.getVal().equals(value)){
                anEnum = item;
                break;
            }
        }
        return anEnum;
    }
}

package com.im.common.entity.enums;

/**
 * APP回调操作日志内容
 */
public enum PortalUserAppOperationLogContentEnum implements BaseEnum {
    FRIEND_ADD("FRIEND_ADD", "[ {} ]添加好友[ {} ]"),
    FRIEND_DELETE("FRIEND_DELETE", "[ {} ]删除好友[ {} ]"),

    BLACKLIST_ADD("BLACKLIST_ADD", "[ {} ]将[ {} ]加入到黑名单"),
    BLACKLIST_DELETE("BLACKLIST_DELETE", "[ {} ]将[ {} ]移出黑名单"),

    GROUP_CREATE("GROUP_CREATE", "[ {} ]创建群组[ {} ]"),
    GROUP_INFO_CHANGED("GROUP_INFO_CHANGED", "[ {} ]修改群组[ {} ]信息"),
    GROUP_DESTROY("GROUP_DESTROY", "[ {} ]解散群组[ {} ]"),
    GROUP_MEMBER_JOIN("GROUP_MEMBER_JOIN", "[ {} ]邀请[[ {} ]]加入群组[ {} ]"),
    GROUP_MEMBER_EXIT("GROUP_MEMBER_EXIT", "[ {} ]主动退出群组[ {} ]"),
    GROUP_MEMBER_EXIT_KICK("GROUP_MEMBER_EXIT_KICK", "[ {} ]将[ {} ]踢出群组[ {} ]"),
    ;

    private String val;
    private String str;

    PortalUserAppOperationLogContentEnum(String val, String str) {
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

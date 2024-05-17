package com.im.common.entity.enums;

public enum TiSendMsgControlEnum implements BaseEnum {
    NoUnread("NoUnread", "表示该条消息不计入未读数"),
    NoLastMsg("NoLastMsg", "表示该条消息不更新会话列表"),
    WithMuteNotifications("WithMuteNotifications", "表示该条消息的接收方对发送方设置的免打扰选项生效（默认不生效）"),
    ;

    private String val;
    private String str;

    TiSendMsgControlEnum(String val, String str) {
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

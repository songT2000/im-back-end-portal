package com.im.common.entity.enums;

public enum ActionEnum implements BaseEnum{

    Login("Login","上线"),
    Logout("Logout","下线"),
    Disconnect("Disconnect","网络断开"),
    ;
    private String val;
    private String str;

    ActionEnum(String val, String str) {
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

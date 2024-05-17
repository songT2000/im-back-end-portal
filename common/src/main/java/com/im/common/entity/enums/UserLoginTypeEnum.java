package com.im.common.entity.enums;

/**
 * 登录方式
 *
 * @author Barry
 * @date 2021-01-19
 */
public enum UserLoginTypeEnum implements BaseEnum {
    /**
     * 用户名密码登录
     **/
    MANUAL("1", "用户名密码登录"),

    /**
     * TOKEN自动登录
     **/
    TOKEN("2", "TOKEN自动登录");

    private String val;
    private String str;

    UserLoginTypeEnum(String val, String str) {
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

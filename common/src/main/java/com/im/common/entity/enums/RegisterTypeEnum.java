package com.im.common.entity.enums;

/**
 * 前台用户->注册方式
 *
 * @author Barry
 * @date 2019-10-11
 */
public enum RegisterTypeEnum implements BaseEnum {
    /**
     * 首页注册
     **/
    HOME_REGISTER("1", "首页注册"),

    /**
     * 邀请码注册
     **/
    INVITE_CODE_REGISTER("2", "邀请码注册"),

    /**
     * 后台开户
     **/
    ADMIN_CREATE("3", "后台开户");

    private String val;
    private String str;

    RegisterTypeEnum(String val, String str) {
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

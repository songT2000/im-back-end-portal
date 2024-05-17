package com.im.common.constant;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 供应商端session Key
 *
 * @author Barry
 * @date 2019-10-10
 */
public enum RedisSessionKeyEnum implements IEnum<String> {
    /**
     * 用户
     **/
    USER("USER"),

    /**
     * 登出，对应前台PortalResponseCode
     **/
    LOGOUT_CODE("LOGOUT_CODE"),

    /**
     * 上次访问时间
     **/
    LAST_ACCESS_TIME("LAST_ACCESS_TIME");

    private String val;

    RedisSessionKeyEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    @Override
    public String getValue() {
        return this.val;
    }

    @Override
    public String toString() {
        return this.val;
    }
}

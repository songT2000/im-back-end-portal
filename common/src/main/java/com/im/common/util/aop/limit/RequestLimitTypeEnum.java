package com.im.common.util.aop.limit;


import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 限速类型
 *
 * @author Barry
 * @date 2019-11-30
 */
public enum RequestLimitTypeEnum implements IEnum<String> {
    /**
     * IP
     **/
    IP("1"),

    /**
     * USER
     **/
    USER("2"),

    /**
     * 全局
     **/
    GLOBAL("23"),

    /**
     * 请求body key，设置此项时，@RequestLimit必须设置requestBodyKey
     **/
    REQUEST_BODY_KEY("4");

    private String val;

    RequestLimitTypeEnum(String val) {
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

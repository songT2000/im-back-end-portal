package com.im.common.cache.sysconfig.bo;

import com.im.common.entity.enums.BaseEnum;

/**
 * MQTT类型
 *
 * @author Barry
 * @date 2021-03-01
 */
public enum MqttTypeEnum implements BaseEnum {
    /**
     * 阿里云
     **/
    ALIYUN("aliyun", "阿里云");

    private String val;
    private String str;

    MqttTypeEnum(String val, String str) {
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

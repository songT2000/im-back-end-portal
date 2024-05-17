package com.im.common.entity.enums;

import com.alibaba.fastjson.annotation.JSONField;

import java.beans.Transient;

/**
 * 设备类型
 *
 * @author Barry
 * @date 2019-10-11
 */
public enum DeviceTypeEnum implements BaseEnum {
    /**
     * PC
     **/
    PC("1", "PC"),

    /**
     * H5
     **/
    H5("2", "H5"),

    /**
     * Android
     **/
    ANDROID("3", "Android"),

    /**
     * iOS
     **/
    IOS("4", "iOS"),

    /**
     * 未知
     **/
    UNKNOWN("5", "未知");

    private String val;
    private String str;

    DeviceTypeEnum(String val, String str) {
        this.val = val;
        this.str = str;
    }

    @JSONField(serialize = false)
    @Transient
    public static boolean isWeb(DeviceTypeEnum deviceType) {
        return deviceType == PC || deviceType == H5;
    }

    @JSONField(serialize = false)
    @Transient
    public static boolean isMobile(DeviceTypeEnum deviceType) {
        return deviceType == IOS || deviceType == ANDROID;
    }

    @JSONField(serialize = false)
    @Transient
    public static boolean isSameTypeDevice(DeviceTypeEnum deviceType1, DeviceTypeEnum deviceType2) {
        return (isWeb(deviceType1) && isWeb(deviceType2)) || (isMobile(deviceType1) && isMobile(deviceType2));
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

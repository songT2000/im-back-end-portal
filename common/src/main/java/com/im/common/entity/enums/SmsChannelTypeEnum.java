package com.im.common.entity.enums;

/**
 * 短信信令通道类型
 *
 * @author Barry
 * @date 2022-02-10
 */
public enum SmsChannelTypeEnum implements BaseEnum {
    /**
     * 阿里云
     **/
    ALIYUN("ALIYUN", "阿里云"),
    /**
     * 杂牌，http://39.105.59.44:1703/
     **/
    QYSS("QYSS", "QYSS");

    private String val;
    private String str;

    SmsChannelTypeEnum(String val, String str) {
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

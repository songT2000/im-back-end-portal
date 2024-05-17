package com.im.common.cache.sysconfig.bo;

import com.im.common.entity.enums.BaseEnum;

/**
 * OSS类型
 *
 * @author Barry
 * @date 2020-08-07
 */
public enum OssTypeEnum implements BaseEnum {
    /**
     * 阿里云
     **/
    ALIYUN("aliyun", "阿里云"),
    MINIO("minio", "自建minio");

    private String val;
    private String str;

    OssTypeEnum(String val, String str) {
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

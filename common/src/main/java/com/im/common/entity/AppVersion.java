package com.im.common.entity;

import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.AppTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * app版本管理
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppVersion extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -109926666932463123L;


    /**
     * 应用类型，ios或者android
     */
    private AppTypeEnum appType;

    /**
     * 版本号
     */
    private String versionName;

    /**
     * 版本数字代号，用于对比
     */
    private int versionCode;

    /**
     * 是否强制升级
     */
    private Boolean compulsory;

    /**
     * 版本说明
     */
    private String note;

    /**
     * 下载地址
     */
    private String downloadUrl;
}

package com.im.common.util.api.im.tencent.request.api;

import lombok.Getter;

/**
 * 账号管理
 */
@Getter
public enum PortraitApiEnum {

    portrait_set("v4/profile/portrait_set","设置资料"),
    portrait_get("v4/profile/portrait_get","拉取资料");

    /**
     * 接口地址
     */
    private String url;
    /**
     * 备注
     */
    private String remark;

    PortraitApiEnum(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }
}

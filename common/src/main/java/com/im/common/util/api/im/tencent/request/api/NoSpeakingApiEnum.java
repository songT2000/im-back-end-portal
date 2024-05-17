package com.im.common.util.api.im.tencent.request.api;

import lombok.Getter;

/**
 * 禁言管理
 */
@Getter
public enum NoSpeakingApiEnum {

    getnospeaking("v4/openconfigsvr/getnospeaking","查询禁言"),
    setnospeaking("v4/openconfigsvr/setnospeaking","设置禁言"),
    ;
    /**
     * 接口地址
     */
    private String url;
    /**
     * 备注
     */
    private String remark;

    NoSpeakingApiEnum(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }
}

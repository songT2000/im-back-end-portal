package com.im.common.util.api.im.tencent.request.api;

import lombok.Getter;

/**
 * 运营管理
 */
@Getter
public enum OperationApiEnum {

    getappinfo("v4/openconfigsvr/getappinfo","拉取运营数据"),
    get_history("v4/open_msg_svc/get_history","下载最近消息记录"),
    ;

    /**
     * 接口地址
     */
    private String url;
    /**
     * 备注
     */
    private String remark;

    OperationApiEnum(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }
}

package com.im.callback.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 回掉函数参数基础类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TiCallbackRequest implements Serializable {

    private static final long serialVersionUID = -4070808686525549267L;
    /**
     * 回调命令
     */
    @JSONField(name = "CallbackCommand")
    private String callbackCommand;
    /**
     * App 在即时通信 IM 分配的应用标识
     */
    private String sdkAppId;
    /**
     * 可选，通常值为 JSON
     */
    private String contentType;
    /**
     * 客户端 IP 地址
     */
    private String clientIp;
    /**
     * 客户端平台，对应不同的平台类型，可能的取值有：RESTAPI（使用 REST API 发送请求）、Web（使用 Web SDK 发送请求）、
     * Android、iOS、Windows、Mac、IPad、Unknown（使用未知类型的设备发送请求）
     */
    private String optPlatform;

}

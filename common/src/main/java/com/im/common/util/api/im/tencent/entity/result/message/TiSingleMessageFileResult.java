package com.im.common.util.api.im.tencent.entity.result.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class TiSingleMessageFileResult implements Serializable {

    /**
     * 消息发送方 UserID（用于指定发送消息方帐号）
     */
    @JSONField(name = "From_Account")
    private String fromAccount;

    /**
     * 消息接收方 UserID（必填）
     */
    @JSONField(name = "To_Account")
    private String toAccount;
    /**
     * 消息随机数，后台用于同一秒内的消息去重。请确保该字段填的是随机数（必填）
     */
    @JSONField(name = "MsgRandom")
    private Long msgRandom;
    /**
     * 消息序列号，后台会根据该字段去重及进行同秒内消息的排序，详细规则请看本接口的功能说明。若不填该字段，则由后台填入随机数
     */
    @JSONField(name = "MsgSeq")
    private Long msgSeq;
    /**
     * 消息时间戳，UNIX 时间戳（单位：秒）
     */
    @JSONField(name = "MsgTimestamp")
    private Long msgTimestamp;
    /**
     * 消息发送平台
     */
    @JSONField(name = "MsgFromPlatform")
    private String msgFromPlatform;
    /**
     * 消息发送者的IP
     */
    @JSONField(name = "ClientIP")
    private String clientIp;
    /**
     * 标识该条消息，可用于 REST API 撤回单聊消息
     */
    @JSONField(name = "MsgKey")
    private String msgKey;
    /**
     * 消息内容
     */
    @JSONField(name = "MsgBody")
    private List<TiMsgBody> msgBody;
}

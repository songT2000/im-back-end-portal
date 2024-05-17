package com.im.common.util.api.im.tencent.entity.result.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 群聊消息文件返回结构
 */
@Data
@NoArgsConstructor
public class TiGroupMessageFileResult implements Serializable {
    /**
     * 消息的发送者
     */
    @JSONField(name = "From_Account")
    private String fromAccount;
    /**
     * 消息 seq，用于标识唯一消息，值越小发送的越早
     */
    @JSONField(name = "MsgSeq")
    private Long msgSeq;
    /**
     * 消息被发送的时间戳，server 的时间
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
     * 消息内容
     */
    @JSONField(name = "MsgBody")
    private List<TiMsgBody> msgBody;

}

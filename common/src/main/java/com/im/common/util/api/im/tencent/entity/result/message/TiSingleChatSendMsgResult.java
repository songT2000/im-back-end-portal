package com.im.common.util.api.im.tencent.entity.result.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单聊消息发送返回结果
 */
@Data
@NoArgsConstructor
public class TiSingleChatSendMsgResult extends TiBaseResult {

    /**
     * 消息时间戳，UNIX 时间戳
     */
    @JSONField(name = "MsgTime")
    private Integer msgTime;
    /**
     * 消息唯一标识，用于撤回。长度不超过50个字符
     */
    @JSONField(name = "MsgKey")
    private String msgKey;
}

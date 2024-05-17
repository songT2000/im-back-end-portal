package com.im.common.util.api.im.tencent.entity.result.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TiSingleMessageSendResult implements Serializable {

    private static final long serialVersionUID = -3118897561229226125L;

    /**
     * 消息时间戳，UNIX 时间戳（单位：秒）
     */
    @JSONField(name = "MsgTime")
    private Long msgTime;
    /**
     * 标识该条消息，可用于 REST API 撤回单聊消息
     */
    @JSONField(name = "MsgKey")
    private String msgKey;


}

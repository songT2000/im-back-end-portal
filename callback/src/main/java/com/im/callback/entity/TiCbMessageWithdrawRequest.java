package com.im.callback.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单聊消息撤回后的回掉
 */
@NoArgsConstructor
@Data
public class TiCbMessageWithdrawRequest extends TiCallbackRequest {

    private static final long serialVersionUID = -8799683464463345620L;
    /**
     * 消息接收方 UserID
     */
    @JSONField(name = "To_Account")
    private String toAccount;
    /**
     * 消息发送方 UserID
     */
    @JSONField(name = "From_Account")
    private String fromAccount;
    /**
     * 消息的唯一标识
     */
    @JSONField(name = "MsgKey")
    private String msgKey;
    /**
     * To_Account 未读的单聊消息总数量（包含所有的单聊会话）
     */
    @JSONField(name = "UnreadMsgNum")
    private Integer unreadMsgNum;
}

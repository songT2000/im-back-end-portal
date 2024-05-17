package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 撤回消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiWithdrawMessageParam implements Serializable {

    /**
     * 消息发送方 UserID（必填）
     */
    @JSONField(name = "From_Account")
    private String fromAccount;
    /**
     * 消息接收方 UserID（必填）
     */
    @JSONField(name = "To_Account")
    private String toAccount;
    /**
     * 待撤回消息的唯一标识。该字段由 REST API 接口 单发单聊消息 和 批量发单聊消息 返回（必填）
     */
    @JSONField(name = "MsgKey")
    private String msgKey;
}

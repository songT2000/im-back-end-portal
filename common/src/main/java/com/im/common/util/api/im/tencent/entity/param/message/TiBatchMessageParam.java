package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 批量发送腾讯IM单聊消息体
 * <br>注意：此类只包括必填的参数，完整的请参考
 *
 * @see TiMessageOptional
 */
@Data
@NoArgsConstructor
public class TiBatchMessageParam implements Serializable {
    /**
     * 消息接收方 UserID集合（必填）
     */
    @JSONField(name = "To_Account")
    private List<String> toAccount;
    /**
     * 消息随机数，后台用于同一秒内的消息去重。请确保该字段填的是随机数（必填）
     */
    @JSONField(name = "MsgRandom")
    private Integer msgRandom;

    /**
     * 消息内容（必填），具体格式请参考
     *
     * @see TiMsgBody
     * （注意，一条消息可包括多种消息元素，MsgBody 为 集合类型）
     */
    @JSONField(name = "MsgBody")
    private List<TiMsgBody> msgBody;

}

package com.im.common.util.api.im.tencent.entity.result.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class TiSingleMessageResult implements Serializable {

    private static final long serialVersionUID = -3118897561229226125L;
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
    @JSONField(name = "MsgTimeStamp")
    private Long msgTimeStamp;
    /**
     * 该条消息的属性，0表示正常消息，8表示被撤回的消息
     */
    @JSONField(name = "MsgFlagBits")
    private Integer msgFlagBits;
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
    /**
     * 消息自定义数据（云端保存，会发送到对端，程序卸载重装后还能拉取到）
     */
    @JSONField(name = "CloudCustomData")
    private String cloudCustomData;

}

package com.im.callback.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 单聊消息发送前后回调
 */
@Data
@NoArgsConstructor
public class TiCbMessageSendRequest extends TiCallbackRequest {
    private static final long serialVersionUID = -1446232003675486L;
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
     * 消息的唯一标识，可用于 REST API 撤回单聊消息
     */
    @JSONField(name = "MsgKey")
    private String msgKey;
    /**
     * 消息序列号，后台会根据该字段去重及进行同秒内消息的排序，详细规则请看本接口的功能说明。若不填该字段，则由后台填入随机数
     */
    @JSONField(name = "MsgSeq")
    private Long msgSeq;
    /**
     * 消息时间戳，UNIX 时间戳（单位：秒）
     */
    @JSONField(name = "MsgTime")
    private Long msgTime;

    /**
     * 消息随机数
     */
    @JSONField(name = "MsgRandom")
    private Long msgRandom;

    /**
     * 在线消息，为1，否则为0
     */
    @JSONField(name = "OnlineOnlyFlag")
    private Integer onlineOnlyFlag;
    /**
     * 该条消息的下发结果，0表示下发成功，非0表示下发失败，具体可参见 错误码
     */
    @JSONField(name = "SendMsgResult")
    private Integer sendMsgResult;
    /**
     * 该条消息下发失败的错误信息，若消息发送成功，则为"send msg succeed"
     */
    @JSONField(name = "ErrorInfo")
    private String errorInfo;
    /**
     * To_Account 未读的单聊消息总数量（包含所有的单聊会话）。若该条消息下发失败（例如被脏字过滤），该字段值为-1
     */
    @JSONField(name = "UnreadMsgNum")
    private Integer unreadMsgNum;

    /**
     * 消息内容，具体格式请参考
     *
     * @see TiMsgBody
     * （注意，一条消息可包括多种消息元素，MsgBody 为 集合类型）
     */
    @JSONField(name = "MsgBody")
    private List<TiMsgBody> msgBody;

    /**
     * 自定义消息内容，建议存放json数据
     */
    @JSONField(name = "CloudCustomData")
    private String cloudCustomData;


}

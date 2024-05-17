package com.im.callback.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.enums.GroupTypeEnum;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 群组消息发送前后回调
 */
@Data
@NoArgsConstructor
public class TiCbGroupMessageSendRequest extends TiCallbackRequest {
    private static final long serialVersionUID = -6764923301814017241L;
    /**
     * 群组ID
     */
    @JSONField(name = "GroupId")
    private String groupId;
    /**
     * 消息发送方 UserID
     */
    @JSONField(name = "From_Account")
    private String fromAccount;
    /**
     * 操作者 UserID
     */
    @JSONField(name = "Operator_Account")
    private String operatorAccount;
    /**
     * 群主 UserID
     */
    @JSONField(name = "Owner_Account")
    private String ownerAccount;
    /**
     * 群组类型介绍，例如 Public
     */
    @JSONField(name = "Type")
    private GroupTypeEnum type;
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
    @JSONField(name = "Random")
    private Long random;

    /**
     * 在线消息，为1，否则为0
     */
    @JSONField(name = "OnlineOnlyFlag")
    private Integer onlineOnlyFlag;

    /**
     * 消息内容，具体格式请参考
     *
     * @see TiMsgBody
     * （注意，一条消息可包括多种消息元素，MsgBody 为 集合类型）
     */
    @JSONField(name = "MsgBody")
    private List<TiMsgBody> msgBody;

}

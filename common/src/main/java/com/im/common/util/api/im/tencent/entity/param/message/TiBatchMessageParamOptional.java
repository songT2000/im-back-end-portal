package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.enums.TiSendMsgControlEnum;
import com.im.common.entity.enums.TiSyncOtherMachineEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量腾讯IM单聊消息体完整版
 */
@Data
@NoArgsConstructor
public class TiBatchMessageParamOptional extends TiBatchMessageParam {
    /**
     * 1：把消息同步到 From_Account 在线终端和漫游上；
     * 2：消息不同步至 From_Account；
     * 若不填写默认情况下会将消息存 From_Account 漫游
     * <br>
     *
     * @see TiSyncOtherMachineEnum
     */
    @JSONField(name = "SyncOtherMachine")
    private TiSyncOtherMachineEnum syncOtherMachine;
    /**
     * 消息发送方 UserID（用于指定发送消息方帐号）
     */
    @JSONField(name = "From_Account")
    private String fromAccount;
    /**
     * 消息离线保存时长（单位：秒），最长为7天（604800秒）
     * <br>若设置该字段为0，则消息只发在线用户，不保存离线
     * <br>若设置该字段超过7天（604800秒），仍只保存7天
     * <br>若不设置该字段，则默认保存7天
     */
    @JSONField(name = "MsgLifeTime")
    private Integer msgLifeTime = 604800;
    /**
     * 消息序列号，后台会根据该字段去重及进行同秒内消息的排序，详细规则请看本接口的功能说明。若不填该字段，则由后台填入随机数
     */
    @JSONField(name = "MsgSeq")
    private Integer msgSeq;
    /**
     * 消息时间戳，UNIX 时间戳（单位：秒）
     */
    @JSONField(name = "MsgTimeStamp")
    private Integer msgTimeStamp;

    /**
     * 消息发送控制选项，是一个 String 数组，只对本条消息有效。"NoUnread"表示该条消息不计入未读数。"NoLastMsg"表示该条消息不更新会话列表。
     * "WithMuteNotifications"表示该条消息的接收方对发送方设置的免打扰选项生效（默认不生效）。
     * 示例："SendMsgControl": ["NoUnread","NoLastMsg","WithMuteNotifications"]
     * <br>
     *
     * @see TiSendMsgControlEnum
     */
    @JSONField(name = "SendMsgControl")
    private List<TiSendMsgControlEnum> sendMsgControl;

    /**
     * 自定义消息内容，建议存放json数据
     */
    @JSONField(name = "CloudCustomData")
    private String cloudCustomData;
    /**
     * 离线推送配置信息
     */
    @JSONField(name = "OfflinePushInfo")
    private TiOfflinePushInfoParam offlinePushInfo;


}

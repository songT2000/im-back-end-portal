package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.enums.TiMsgTypeEnum;
import com.im.common.util.RandomUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 腾讯IM单聊消息体
 * <br>注意：此类只包括必填的参数，完整的请参考
 *
 * @see TiMessageOptional
 */
@Data
@NoArgsConstructor
public class TiMessage implements Serializable {
    /**
     * 选填
     * <p>
     * 1：把消息同步到 From_Account 在线终端和漫游上；
     * 2：消息不同步至 From_Account；
     * 若不填写默认情况下会将消息存 From_Account 漫游
     */
    @JSONField(name = "SyncOtherMachine")
    private String syncOtherMachine;

    /**
     * 选填
     * <p>
     * 消息发送方 UserID（不填就是管理员）
     */
    @JSONField(name = "From_Account")
    private String fromAccount;

    /**
     * 必填
     * <p>
     * 消息接收方 UserID
     */
    @JSONField(name = "To_Account")
    private String toAccount;

    /**
     * 选填
     * <p>
     * 消息离线保存时长（单位：秒），最长为7天（604800秒）
     * 若设置该字段为0，则消息只发在线用户，不保存离线
     * 若设置该字段超过7天（604800秒），仍只保存7天
     * 若不设置该字段，则默认保存7天
     */
    @JSONField(name = "MsgLifeTime")
    private Integer msgLifeTime;

    /**
     * 选填
     * <p>
     * 消息序列号，后台会根据该字段去重及进行同秒内消息的排序，详细规则请看本接口的功能说明。若不填该字段，则由后台填入随机数。
     */
    @JSONField(name = "MsgSeq")
    private Integer msgSeq;

    /**
     * 必填
     * <p>
     * 消息随机数，后台用于同一秒内的消息去重。请确保该字段填的是随机数
     */
    @JSONField(name = "MsgRandom")
    private Integer msgRandom;

    /**
     * 选填
     * <p>
     * 消息时间戳，UNIX 时间戳（单位：秒）
     */
    @JSONField(name = "MsgTimeStamp")
    private Integer msgTimeStamp;

    /**
     * 选填
     * <p>
     * 消息回调禁止开关，只对本条消息有效，ForbidBeforeSendMsgCallback 表示禁止发消息前回调，ForbidAfterSendMsgCallback 表示禁止发消息后回调
     */
    @JSONField(name = "ForbidCallbackControl")
    private Set<String> forbidCallbackControl;

    /**
     * 选填
     * <p>
     * 消息发送控制选项，是一个 String 数组，只对本条消息有效。"NoUnread"表示该条消息不计入未读数。"NoLastMsg"表示该条消息不更新会话列表。
     * "WithMuteNotifications"表示该条消息的接收方对发送方设置的免打扰选项生效（默认不生效）。示例："SendMsgControl": ["NoUnread","NoLastMsg","WithMuteNotifications"]
     */
    @JSONField(name = "SendMsgControl")
    private Set<String> sendMsgControl;

    /**
     * 必填
     * <p>
     * 消息内容，具体格式请参考
     *
     * @see TiMsgBody
     * （注意，一条消息可包括多种消息元素，MsgBody 为 集合类型）
     */
    @JSONField(name = "MsgBody")
    private List<TiMsgBody> msgBody;

    @JSONField(name = "OfflinePushInfo")
    private TiOfflinePushInfoParam offlinePushInfo;

    public TiMessage(String toAccount, Integer msgRandom, List<TiMsgBody> msgBody) {
        this.fromAccount = "administrator";
        this.toAccount = toAccount;
        this.msgRandom = msgRandom;
        this.msgBody = msgBody;
    }

    public TiMessage(String fromAccount, String toAccount, Integer msgRandom, List<TiMsgBody> msgBody) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.msgRandom = msgRandom;
        this.msgBody = msgBody;
    }

    public TiMessage(String fromAccount, String toAccount, TiMsgCustomItem item) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;

        String data = JSON.toJSONString(item);
        TIMCustomElem customElem = new TIMCustomElem();
        customElem.setData(data);
        customElem.setExt(item.getBusinessID());

        TiMsgBody tiMsgBody = new TiMsgBody();
        tiMsgBody.setMsgContent(customElem);
        tiMsgBody.setMsgType(TiMsgTypeEnum.TIMCustomElem);

        this.msgBody = new ArrayList<>();
        this.msgBody.add(tiMsgBody);

        this.msgRandom = RandomUtil.randomInt(1000000);
    }

}

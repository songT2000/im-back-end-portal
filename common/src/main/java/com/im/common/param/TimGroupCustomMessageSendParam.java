package com.im.common.param;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.enums.TiMsgTypeEnum;
import com.im.common.util.RandomUtil;
import com.im.common.util.api.im.tencent.entity.param.message.TIMCustomElem;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgCustomItem;
import com.im.common.util.api.im.tencent.entity.param.message.TiOfflinePushInfoParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 腾讯IM群聊自定义消息
 */
@Data
@NoArgsConstructor
public class TimGroupCustomMessageSendParam implements Serializable {

    private static final long serialVersionUID = -2492971363121270579L;
    /**
     * 消息接收群组ID
     */
    @JSONField(name = "GroupId")
    private String groupId;
    /**
     * 消息优先级
     */
    @JSONField(name = "MsgPriority")
    private String msgPriority = "High";
    /**
     * 消息随机数，后台用于同一秒内的消息去重。
     */
    @JSONField(name = "Random")
    private Integer random;
    /**
     * 禁止回调
     */
    @JSONField(name = "ForbidCallbackControl")
    private List<String> forbidCallbackControl;
    /**
     * 指定 SendMsgControl，设置 NoLastMsg 的情况下，表示不更新最近联系人会话；NoUnread 不计未读
     */
    @JSONField(name = "SendMsgControl")
    private List<String> sendMsgControl;
    /**
     * 消息接收群组ID
     * 消息来源帐号，选填。如果不填写该字段，则默认消息的发送者为调用该接口时使用的 App 管理员帐号。
     * 除此之外，App 亦可通过该字段“伪造”消息的发送者，从而实现一些特殊的功能需求。
     * 需要注意的是，如果指定该字段，必须要确保字段中的帐号是存在的
     */
    @JSONField(name = "From_Account")
    private String fromAccount;
    /**
     * 指定消息接收者(接收者成员上限50个)，如果此字段被使用，消息则不计未读
     */
    @JSONField(name = "To_Account")
    private List<String> toAccount;
    /**
     * 消息内容
     */
    @JSONField(name = "MsgBody")
    private List<TiMsgBody> msgBody = new ArrayList<>();
    @JSONField(name = "OfflinePushInfo")
    private TiOfflinePushInfoParam offlinePushInfo;

    /**
     * 构建群组自定义消息
     *
     * @param groupId 群组ID
     * @param item    自定义数据
     */
    public TimGroupCustomMessageSendParam(String groupId, TiMsgCustomItem item) {
        this.groupId = groupId;

        String data = JSON.toJSONString(item);
        TIMCustomElem customElem = new TIMCustomElem();
        customElem.setData(data);
        customElem.setExt(item.getBusinessID());

        TiMsgBody tiMsgBody = new TiMsgBody();
        tiMsgBody.setMsgContent(customElem);
        tiMsgBody.setMsgType(TiMsgTypeEnum.TIMCustomElem);

        this.msgBody.add(tiMsgBody);

        this.random = RandomUtil.randomInt(1000000);
    }

    /**
     * 发红包消息
     *
     * @param groupId     群组ID
     * @param item        红包消息内容
     * @param fromAccount 发红包用户的账号
     */
    public TimGroupCustomMessageSendParam(String groupId, TiMsgCustomItem item, String fromAccount) {
        this.groupId = groupId;
        this.fromAccount = fromAccount;

        String data = JSON.toJSONString(item);
        TIMCustomElem customElem = new TIMCustomElem();
        customElem.setData(data);
        customElem.setExt(item.getBusinessID());

        TiMsgBody tiMsgBody = new TiMsgBody();
        tiMsgBody.setMsgContent(customElem);
        tiMsgBody.setMsgType(TiMsgTypeEnum.TIMCustomElem);

        this.msgBody.add(tiMsgBody);

        this.random = RandomUtil.randomInt(1000000);
        this.setSendMsgControl(null);
        this.setForbidCallbackControl(null);
    }

    /**
     * 退回红包消息
     *
     * @param groupId   群组ID
     * @param item      红包消息内容
     * @param toAccount 要退回红包的用户的账号
     */
    public TimGroupCustomMessageSendParam(String groupId, TiMsgCustomItem item, List<String> toAccount) {
        this.groupId = groupId;
        this.toAccount = toAccount;

        String data = JSON.toJSONString(item);
        TIMCustomElem customElem = new TIMCustomElem();
        customElem.setData(data);
        customElem.setExt(item.getBusinessID());

        TiMsgBody tiMsgBody = new TiMsgBody();
        tiMsgBody.setMsgContent(customElem);
        tiMsgBody.setMsgType(TiMsgTypeEnum.TIMCustomElem);

        this.msgBody.add(tiMsgBody);

        this.random = RandomUtil.randomInt(1000000);
        this.setSendMsgControl(null);
        this.setForbidCallbackControl(null);
    }
}

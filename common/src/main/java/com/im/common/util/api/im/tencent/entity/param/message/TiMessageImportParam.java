package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.LongCodec;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.tim.TimMessageBody;
import com.im.common.entity.tim.TimMessageC2c;
import com.im.common.util.LocalDateTimeUtil;
import com.im.common.util.TimMessageUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 腾讯IM单聊消息导入
 */
@Data
@NoArgsConstructor
public class TiMessageImportParam implements Serializable {
    private static final long serialVersionUID = 486717547143105430L;
    /**
     * 该字段只能填1或2，其他值是非法值
     * 1表示实时消息导入，消息计入未读计数
     * 2表示历史消息导入，消息不计入未读
     */
    @JSONField(name = "SyncFromOldSystem")
    private Integer syncFromOldSystem = 2;

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
     * 消息序列号，后台会根据该字段去重及进行同秒内消息的排序，详细规则请看本接口的功能说明。若不填该字段，则由后台填入随机数。
     */
    @JSONField(name = "MsgSeq",serializeUsing = LongCodec.class)
    private Long msgSeq;

    /**
     * 必填
     * <p>
     * 消息随机数，后台用于同一秒内的消息去重。请确保该字段填的是随机数
     */
    @JSONField(name = "MsgRandom",serializeUsing = LongCodec.class)
    private Long msgRandom;

    /**
     * 选填
     * <p>
     * 消息时间戳，UNIX 时间戳（单位：秒）
     */
    @JSONField(name = "MsgTimeStamp",serializeUsing = LongCodec.class)
    private Long msgTimeStamp;


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

    public TiMessageImportParam(TimMessageC2c messageC2c, PortalUserCache portalUserCache) {
        this.fromAccount = portalUserCache.getUsernameByIdFromLocal(messageC2c.getFromUserId());
        this.toAccount = portalUserCache.getUsernameByIdFromLocal(messageC2c.getToUserId());
        this.msgRandom = messageC2c.getMsgRandom();
        this.msgSeq = messageC2c.getMsgSeq();
        this.msgTimeStamp = LocalDateTimeUtil.getTimestampOfDateTime(messageC2c.getSendTime())/1000L;
        this.msgBody = messageC2c.getMsgBody().stream().map(p-> TimMessageUtil.convertTimElem(p.getMsgContent())).collect(Collectors.toList());
    }
}

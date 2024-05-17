package com.im.common.util.api.im.tencent.entity.result.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 群聊消息返回结构
 */
@Data
@NoArgsConstructor
public class TiGroupMessageResult implements Serializable {
    /**
     * 消息的发送者
     */
    @JSONField(name = "From_Account")
    private String fromAccount;
    /**
     * 是否是空洞消息，当消息被删除或者消息过期后，MsgBody 为空，该字段为1
     */
    @JSONField(name = "IsPlaceMsg")
    private Integer isPlaceMsg;
    /**
     * 是否系统消息，1为系统消息，MsgBody 为Object，无需备份存储
     */
    @JSONField(name = "IsSystemMsg")
    private Integer isSystemMsg;
    /**
     * 消息的优先级，用于消息去重，有客户端发消息时填写，如果没有填，服务端会自动生成
     * <br>1表示 High 优先级消息，2表示 Normal 优先级消息，3表示 Low 优先级消息，4表示 Lowest 优先级消息
     */
    @JSONField(name = "MsgPriority")
    private Integer msgPriority;
    /**
     * 消息随机值，用于对消息去重，有客户端发消息时填写，如果没有填，服务端会自动生成
     */
    @JSONField(name = "MsgRandom")
    private Long msgRandom;
    /**
     * 消息 seq，用于标识唯一消息，值越小发送的越早
     */
    @JSONField(name = "MsgSeq")
    private Long msgSeq;
    /**
     * 消息被发送的时间戳，server 的时间
     */
    @JSONField(name = "MsgTimeStamp")
    private Long msgTimeStamp;

    /**
     * 消息自定义数据（云端保存，会发送到对端，程序卸载重装后还能拉取到）
     */
    @JSONField(name = "CloudCustomData")
    private String cloudCustomData;
    /**
     * 消息内容
     */
    @JSONField(name = "MsgBody")
    private List<TiMsgBody> msgBody;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TiGroupMessageResult)) {
            return false;
        }
        TiGroupMessageResult that = (TiGroupMessageResult) o;
        return Objects.equals(getMsgSeq(), that.getMsgSeq());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMsgSeq());
    }
}

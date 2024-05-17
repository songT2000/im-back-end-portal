package com.im.common.util.api.im.tencent.entity.param.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.LongCodec;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 导入群组消息参数
 */
@Data
@NoArgsConstructor
public class TiGroupMessageImportParam implements Serializable {

    /**
     * 群组ID(必填)
     */
    @JSONField(name = "GroupId")
    private String groupId;

    /**
     * 会话更新识别，为1的时候标识触发会话更新，默认不触发（avchatroom 群不支持）
     */
    @JSONField(name = "RecentContactFlag")
    private Integer recentContactFlag;

    /**
     * 待添加的群成员数组
     */
    @JSONField(name = "MsgList")
    private List<Message> msgList;

    @NoArgsConstructor
    @Data
    public static class Message {
        /**
         * 指定消息发送者(必填)
         */
        @JSONField(name = "From_Account")
        private String fromAccount;
        /**
         * 32位随机数；如果5分钟内两条消息的随机值相同，后一条消息将被当做重复消息而丢弃(选填)
         */
        @JSONField(name = "Random",serializeUsing = LongCodec.class)
        private Long random;
        /**
         * 消息发送时间(必填)
         */
        @JSONField(name = "SendTime",serializeUsing = LongCodec.class)
        private Long sendTime;
        /**
         * TIM 消息(必填)
         */
        @JSONField(name = "MsgBody")
        private List<TiMsgBody> msgBody;
    }
}

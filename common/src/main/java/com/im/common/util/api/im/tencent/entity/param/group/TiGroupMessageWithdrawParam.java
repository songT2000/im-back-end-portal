package com.im.common.util.api.im.tencent.entity.param.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.LongCodec;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 撤回群组消息参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiGroupMessageWithdrawParam implements Serializable {

    /**
     * 群组ID(必填)
     */
    @JSONField(name = "GroupId")
    private String groupId;

    /**
     * 被撤回的消息 seq 列表，一次请求最多可以撤回10条消息 seq
     */
    @JSONField(name = "MsgSeqList")
    private List<MsgSeq> msgSeqList;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class MsgSeq {
        /**
         * 请求撤回的消息 seq
         */
        @JSONField(name = "MsgSeq",serializeUsing = LongCodec.class)
        private Long MsgSeq;
    }
}

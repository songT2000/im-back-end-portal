package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.LongCodec;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 查询单聊消息请求体
 */
@Data
@NoArgsConstructor
public class TiMessageQueryParam implements Serializable {

    private static final long serialVersionUID = 4016280489819263849L;
    /**
     * 会话其中一方的 UserID，若已指定发送消息方帐号，则为消息发送方（必填）
     */
    @JSONField(name = "From_Account")
    private String fromAccount;
    /**
     * 会话其中一方的 UserID（必填）
     */
    @JSONField(name = "To_Account")
    private String toAccount;
    /**
     * 请求的消息条数
     */
    @JSONField(name = "MaxCnt")
    private Integer maxCnt;
    /**
     * 请求的消息时间范围的最小值，unix时间（必填）
     */
    @JSONField(name = "MinTime",serializeUsing = LongCodec.class)
    private Long minTime;
    /**
     * 请求的消息时间范围的最大值，unix时间（必填）
     */
    @JSONField(name = "MaxTime",serializeUsing = LongCodec.class)
    private Long maxTime;
    /**
     * 上一次拉取到的最后一条消息的 MsgKey，续拉时需要填该字段
     */
    @JSONField(name = "LastMsgKey")
    private String lastMsgKey;

    public TiMessageQueryParam(String fromAccount, String toAccount, Long minTime, Long maxTime) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.maxCnt = Integer.MAX_VALUE;
    }
}

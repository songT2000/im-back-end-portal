package com.im.common.util.api.im.tencent.entity.param.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.LongCodec;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class TiGroupMessageQueryParam implements Serializable {

    public static final int pageSize = 20;

    /**
     * 要拉取历史消息的群组 ID(必填)
     */
    @JSONField(name = "GroupId")
    private String groupId;
    /**
     * 拉取的历史消息的条数，目前一次请求最多返回20条历史消息(必填)
     */
    @JSONField(name = "ReqMsgNumber")
    private Integer reqMsgNumber = pageSize;
    /**
     * 拉取消息的最大 seq(选填)
     */
    @JSONField(name = "ReqMsgSeq",serializeUsing = LongCodec.class)
    private Long reqMsgSeq;

    public TiGroupMessageQueryParam(String groupId) {
        this.groupId = groupId;
    }

    public TiGroupMessageQueryParam(String groupId,Long reqMsgSeq) {
        this.groupId = groupId;
        this.reqMsgSeq = reqMsgSeq;
    }
}

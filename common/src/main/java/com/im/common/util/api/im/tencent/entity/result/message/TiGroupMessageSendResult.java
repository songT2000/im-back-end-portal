package com.im.common.util.api.im.tencent.entity.result.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TiGroupMessageSendResult extends TiBaseResult {

    private static final long serialVersionUID = -7383422842206715709L;
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

}

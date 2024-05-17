package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 设置单聊消息已读
 */
@Data
@NoArgsConstructor
public class TiSetMessageReadParam implements Serializable {

    /**
     * 进行消息已读的用户 UserId（必填）
     */
    @JSONField(name = "Report_Account")
    private String reportAccount;
    /**
     * 进行消息已读的单聊会话的另一方用户 UserId（必填）
     */
    @JSONField(name = "Peer_Account")
    private String peerAccount;

    /**
     * 时间戳（秒），该时间戳之前的消息全部已读。若不填，则取当前时间戳
     */
    @JSONField(name = "MsgReadTime")
    private Integer msgReadTime;
}

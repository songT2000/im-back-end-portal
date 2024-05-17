package com.im.common.util.api.im.tencent.entity.result.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 历史单聊消息返回结构
 */
@Data
@NoArgsConstructor
public class TiSingleChatMessageHistoryResult extends TiBaseResult {
    /**
     * 是否全部拉取，0表示未全部拉取，需要续拉，1表示已全部拉取
     */
    @JSONField(name = "Complete")
    private Integer complete;
    /**
     * 本次拉取到的消息条数
     */
    @JSONField(name = "MsgCnt")
    private Integer msgCnt;
    /**
     * 本次拉取到的消息里的最后一条消息的时间
     */
    @JSONField(name = "LastMsgTime")
    private Long lastMsgTime;
    /**
     * 本次拉取到的消息里的最后一条消息的标识
     */
    @JSONField(name = "LastMsgKey")
    private String lastMsgKey;
    /**
     * 返回的消息列表
     */
    @JSONField(name = "MsgList")
    private List<TiSingleMessageResult> msgList;

}

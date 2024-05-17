package com.im.common.util.api.im.tencent.entity.result.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询单聊未读消息计数
 */
@Data
@NoArgsConstructor
public class TiSingleChatUnreadMsgNumResult extends TiBaseResult {

    /**
     * 未读的单聊消息的总数
     */
    @JSONField(name = "AllC2CUnreadMsgNum")
    private Integer unreadMsgNum;
}

package com.im.common.util.api.im.tencent.entity.result.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 历史单聊消息返回结构
 */
@Data
@NoArgsConstructor
public class TiGroupMessageHistoryResult extends TiBaseResult {

    @JSONField(name = "GroupId")
    private String groupId;
    /**
     * 是否返回了请求区间的全部消息
     * <li>当成功返回了请求区间的全部消息时，值为1</li>
     * <li>当消息长度太长或者区间太大（超过20）导致无法返回全部消息时，值为0</li>
     * <li>当消息长度太长或者区间太大（超过20）且所有消息都过期时，值为2</li>
     */
    @JSONField(name = "IsFinished")
    private Integer isFinished;
    /**
     * 返回的消息列表
     */
    @JSONField(name = "RspMsgList")
    private List<TiGroupMessageResult> msgList;

}

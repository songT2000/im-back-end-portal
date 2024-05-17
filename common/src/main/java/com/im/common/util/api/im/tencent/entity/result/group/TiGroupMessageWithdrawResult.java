package com.im.common.util.api.im.tencent.entity.result.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 撤回群聊消息返回结构
 */
@Data
@NoArgsConstructor
public class TiGroupMessageWithdrawResult extends TiBaseResult {
    /**
     * 消息撤回请求的详细结果
     */
    @JSONField(name = "RecallRetList")
    private List<RecallRet> recallRets;

    @NoArgsConstructor
    @Data
    public static class RecallRet{

        /**
         * 单个被撤回消息的 seq
         */
        @JSONField(name = "MsgSeq")
        private Long msgSeq;

        /**
         * 单个消息的被撤回结果：0表示成功；其它表示失败
         */
        @JSONField(name = "RetCode")
        private Integer retCode;

    }

}

package com.im.common.util.api.im.tencent.entity.result.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 导入群组消息返回
 */
@Data
@NoArgsConstructor
public class TiGroupMessageImportResult extends TiBaseResult {

    /**
     * 具体的消息导入结果
     */
    @JSONField(name = "ImportMsgResult")
    private List<ImportResult> importResults;

    @NoArgsConstructor
    @Data
    public static class ImportResult {
        /**
         * 单条消息导入结果
         * <br>0表示单条消息成功
         * <br>10004表示单条消息发送时间无效
         * <br>80001表示单条消息包含脏字，拒绝存储此消息
         * <br>80002表示为消息内容过长，目前支持8000字节的消息，请调整消息长度
         */
        @JSONField(name = "Result")
        private Integer result;
        /**
         * 消息序列号，唯一标示一条消息
         */
        @JSONField(name = "MsgSeq")
        private Integer msgSeq;
        /**
         * 消息的时间戳
         */
        @JSONField(name = "MsgTime")
        private Integer msgTime;
    }
}

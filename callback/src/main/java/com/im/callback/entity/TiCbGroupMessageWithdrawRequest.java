package com.im.callback.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 群聊消息撤回后的回掉
 */
@NoArgsConstructor
@Data
public class TiCbGroupMessageWithdrawRequest extends TiCallbackRequest {

    private static final long serialVersionUID = 5893808560587156183L;


    @JSONField( name = "Type")
    private String type;
    /**
     * 撤回的消息msgSeq
     */
    @JSONField( name = "MsgSeqList")
    private List<MsgSeqListDTO> msgSeqList;
    /**
     * 撤回时间
     */
    @JSONField( name = "EventTime")
    private String eventTime;
    /**
     * 操作用户
     */
    @JSONField( name = "Operator_Account")
    private String operatorAccount;
    /**
     * 群组ID
     */
    @JSONField( name = "GroupId")
    private String groupId;

    @NoArgsConstructor
    @Data
    public static class MsgSeqListDTO {
        @JSONField( name = "MsgSeq")
        private Long msgSeq;
    }
}

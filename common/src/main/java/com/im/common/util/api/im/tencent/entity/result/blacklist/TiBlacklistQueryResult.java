package com.im.common.util.api.im.tencent.entity.result.blacklist;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TiBlacklistQueryResult  extends TiBaseResult {

    /**
     * 黑名单对象数组，每一个黑名单对象都包括了 To_Account 和 AddBlackTimeStamp
     */
    @JSONField(name = "BlackListItem")
    private List<BlackListItemDTO> blackListItem;
    /**
     * 下页拉取的起始位置，0表示已拉完
     */
    @JSONField(name = "StartIndex")
    private Integer startIndex;
    /**
     * 黑名单最新的 Seq
     */
    @JSONField(name = "CurruentSequence")
    private Long currentSequence;

    @NoArgsConstructor
    @Data
    public static class BlackListItemDTO {
        /**
         * 黑名单的 UserID
         */
        @JSONField(name = "To_Account")
        private String toAccount;
        /**
         * 添加黑名单的时间
         */
        @JSONField(name = "AddBlackTimeStamp")
        private Long addBlackTimeStamp;
    }
}

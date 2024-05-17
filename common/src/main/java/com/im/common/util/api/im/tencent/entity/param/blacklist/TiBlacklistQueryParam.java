package com.im.common.util.api.im.tencent.entity.param.blacklist;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 删除黑名单参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiBlacklistQueryParam {

    /**
     * 需要拉取该 UserID 的黑名单
     */
    @JSONField(name = "From_Account")
    private String fromAccount;
    /**
     * 拉取的起始位置
     */
    @JSONField(name = "StartIndex")
    private Integer startIndex;
    /**
     * 每页最多拉取的黑名单数
     */
    @JSONField(name = "MaxLimited")
    private Integer maxLimited = 100;
    /**
     * 上一次拉黑名单时后台返回给客户端的 Seq，初次拉取时为0
     */
    @JSONField(name = "LastSequence")
    private Long lastSequence = 0L;

    public TiBlacklistQueryParam(String fromAccount, Integer startIndex, Long lastSequence) {
        this.fromAccount = fromAccount;
        this.startIndex = startIndex;
        this.lastSequence = lastSequence;
    }
}

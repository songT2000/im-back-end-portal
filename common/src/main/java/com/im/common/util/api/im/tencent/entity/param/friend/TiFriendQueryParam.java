package com.im.common.util.api.im.tencent.entity.param.friend;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 拉取好友请求参数
 */
@Data
@NoArgsConstructor
public class TiFriendQueryParam implements Serializable {

    /**
     * 指定要拉取好友数据的用户的 UserID(必填)
     */
    @JSONField(name = "From_Account")
    private String fromAccount;
    /**
     * 分页的起始位置(必填)
     */
    @JSONField(name = "StartIndex")
    private Integer startIndex;
    /**
     * 上次拉好友数据时返回的 StandardSequence，如果 StandardSequence 字段的值与后台一致，后台不会返回标配好友数据（选填）
     */
    @JSONField(name = "StandardSequence")
    private Integer standardSequence = 0;
    /**
     * 上次拉好友数据时返回的 CustomSequence，如果 CustomSequence 字段的值与后台一致，后台不会返回自定义好友数据（选填）
     */
    @JSONField(name = "CustomSequence")
    private Integer customSequence = 0;

    public TiFriendQueryParam(String fromAccount, Integer startIndex) {
        this.fromAccount = fromAccount;
        this.startIndex = startIndex;
    }
}

package com.im.common.util.api.im.tencent.entity.param.friend;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.enums.AddSourceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 删除好友参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiFriendDeleteParam {

    public TiFriendDeleteParam(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public TiFriendDeleteParam(String fromAccount, List<String> toAccounts) {
        this.fromAccount = fromAccount;
        this.toAccounts = toAccounts;
    }

    /**
     * 需要为该 UserID 添加好友(必填)
     */
    @JSONField(name = "From_Account")
    private String fromAccount;
    /**
     * 待删除的好友的 UserID 列表，单次请求的 To_Account 数不得超过1000
     */
    @JSONField(name = "To_Account")
    private List<String> toAccounts;

    /**
     * 删除模式，系统只支持双向模式
     */
    @JSONField(name = "DeleteType")
    private String deleteType = "Delete_Type_Both";

}

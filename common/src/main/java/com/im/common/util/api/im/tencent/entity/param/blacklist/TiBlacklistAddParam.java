package com.im.common.util.api.im.tencent.entity.param.blacklist;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 添加黑名单参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiBlacklistAddParam {

    /**
     * 需要为该 UserID 添加黑名单(必填)
     */
    @JSONField(name = "From_Account")
    private String fromAccount;
    /**
     * 黑名单结构体
     */
    @JSONField(name = "To_Account")
    private List<String> toAccounts;
}

package com.im.common.util.api.im.tencent.entity.result.blacklist;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 删除黑名单结果
 */
@Data
@NoArgsConstructor
public class TiBlacklistDeleteResult extends TiBaseResult {
    /**
     * 返回处理失败的用户列表，仅当存在失败用户时才返回该字段
     */
    @JSONField(name = "Fail_Account")
    private List<String> failAccounts;
    /**
     * 黑名单的结果对象数组
     */
    @JSONField(name = "ResultItem")
    private List<ResultItem> resultItems;

    /**
     * 黑名单的结果对象
     */
    @Data
    @NoArgsConstructor
    public static class ResultItem {
        /**
         * 请求删除的黑名单的 UserID
         */
        @JSONField(name = "To_Account")
        private String toAccount;
        /**
         * To_Account 的处理结果，0表示成功，非0表示失败
         */
        @JSONField(name = "ResultCode")
        private Integer resultCode;
        /**
         * To_Account 的错误描述信息，成功时该字段为空
         */
        @JSONField(name = "ResultInfo")
        private String resultInfo;
    }
}

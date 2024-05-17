package com.im.common.util.api.im.tencent.entity.result.friend;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 添加好友参数
 */
@Data
@NoArgsConstructor
public class TiFriendImportResult extends TiBaseResult {
    /**
     * 返回处理失败的用户列表，仅当存在失败用户时才返回该字段
     */
    @JSONField(name = "Fail_Account")
    private List<String> failAccounts;
    /**
     * 批量加好友的结果对象数组
     */
    @JSONField(name = "ResultItem")
    private List<ResultItem> resultItems;

    /**
     * 批量加好友的结果对象
     */
    @Data
    @NoArgsConstructor
    public static class ResultItem {
        /**
         * 请求添加的好友的 UserID
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

package com.im.common.util.api.im.tencent.entity.result.friend;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 删除好友结果
 */
@Data
@NoArgsConstructor
public class TiFriendDeleteResult extends TiBaseResult {
    /**
     * 删除好友的结果对象数组
     */
    @JSONField(name = "ResultItem")
    private List<ResultItem> resultItems;

    /**
     * 删除好友的结果对象
     */
    @Data
    @NoArgsConstructor
    public static class ResultItem {
        /**
         * 请求删除的好友的 UserID
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

package com.im.common.util.api.im.tencent.entity.result.account;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class TiAccountCheckResult extends TiBaseResult {

    /**
     * 检查结果
     */
    @JSONField(name = "ResultItem")
    private List<ResultItemDTO> resultItem;

    @NoArgsConstructor
    @Data
    public static class ResultItemDTO {
        /**
         * 请求检查的帐号的 UserID
         */
        @JSONField(name = "UserID")
        private String userID;
        /**
         * 单个帐号的检查结果：0表示成功，非0表示失败
         */
        @JSONField(name = "ResultCode")
        private Integer resultCode;
        /**
         * 单个帐号检查失败时的错误描述信息
         */
        @JSONField(name = "ResultInfo")
        private String resultInfo;
        /**
         * 单个帐号的导入状态：Imported 表示已导入，NotImported 表示未导入
         */
        @JSONField(name = "AccountStatus")
        private String accountStatus;
    }
}

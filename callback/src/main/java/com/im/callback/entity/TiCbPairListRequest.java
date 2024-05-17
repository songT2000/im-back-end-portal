package com.im.callback.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 删除好友/添加黑名单/删除黑名单 后的回掉
 */
@NoArgsConstructor
@Data
public class TiCbPairListRequest extends TiCallbackRequest {

    private static final long serialVersionUID = -3638351920550380997L;
    /**
     * 成功删除的好友对
     */
    @JSONField(name = "PairList")
    private List<PairListDTO> pairList;

    @NoArgsConstructor
    @Data
    public static class PairListDTO {
        /**
         * From_Account 的好友表中删除了 To_Account
         */
        @JSONField(name = "From_Account")
        private String fromAccount;
        /**
         * To_Account 从 From_Account 的好友表中删除
         */
        @JSONField(name = "To_Account")
        private String toAccount;
    }
}

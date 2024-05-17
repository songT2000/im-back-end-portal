package com.im.common.util.api.im.tencent.entity.result.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 获取被禁言群成员信息
 */
@Data
@NoArgsConstructor
public class TiGroupShutUpResult extends TiBaseResult {

    /**
     * 群组ID
     */
    @JSONField(name = "GroupId")
    private String groupId;
    /**
     * 禁言用户信息数组
     */
    @JSONField(name = "ShuttedUinList")
    private List<ShuttedUinListDTO> shuttedUinList;

    @NoArgsConstructor
    @Data
    public static class ShuttedUinListDTO {
        /**
         * 被禁言的成员 ID
         */
        @JSONField(name = "Member_Account")
        private String memberAccount;
        /**
         * 被禁言到的时间（UTC时间）
         */
        @JSONField(name = "ShuttedUntil")
        private Integer shuttedUntil;
    }
}

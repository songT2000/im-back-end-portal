package com.im.common.util.api.im.tencent.entity.result.portrait;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.param.portrait.TiAccountPortraitTagParam;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 读用户的资料结果
 */
@Data
@NoArgsConstructor
public class TiPortraitQueryResult extends TiBaseResult {

    /**
     * 返回的用户资料结构化信息
     */
    @JSONField(name = "UserProfileItem")
    private List<UserProfileItemDTO> userProfileItem;
    /**
     * 返回处理失败的用户列表，仅当存在失败用户时才返回该字段
     */
    @JSONField(name = "Fail_Account")
    private List<String> failAccount;

    @NoArgsConstructor
    @Data
    public static class UserProfileItemDTO {
        /**
         * 返回的用户的 UserID
         */
        @JSONField(name = "To_Account")
        private String toAccount;
        /**
         * 返回的用户的资料对象数组，数组中每一个对象都包含了 Tag 和 Value
         */
        @JSONField(name = "ProfileItem")
        private List<TiAccountPortraitTagParam> profileItem;
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

package com.im.common.util.api.im.tencent.entity.result.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.enums.GroupMemberRoleEnum;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 查询群组成员身份返回结构
 */
@Data
@NoArgsConstructor
public class TiGroupMemberRoleResult extends TiBaseResult {
    /**
     * 返回结果为群组成员信息数组
     */
    @JSONField(name = "UserIdList")
    private List<UserIdListDTO> list;

    @NoArgsConstructor
    @Data
    public static class UserIdListDTO {
        /**
         * 成员账号
         */
        @JSONField(name = "Member_Account")
        private String memberAccount;
        /**
         * 拉取到的成员角色，包括：Owner(群主)，Admin(群管理员)，Member(普通群成员），NotMember(非群成员)
         */
        @JSONField(name = "Role")
        private GroupMemberRoleEnum role;
    }
}

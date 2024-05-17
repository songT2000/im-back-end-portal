package com.im.callback.entity;

import com.im.common.entity.enums.GroupTypeEnum;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 群成员离开之后回调
 */
@NoArgsConstructor
@Data
public class TiCbMemberExitRequest extends TiCallbackRequest {

    private static final long serialVersionUID = -3881717959487871790L;
    /**
     * 操作的群 ID
     */
    @JSONField(name = "GroupId")
    private String groupId;
    /**
     * 操作者 UserID
     */
    @JSONField(name = "Operator_Account")
    private String operatorAccount;
    /**
     * 成员离开方式：Kicked-被踢；Quit-主动退群
     */
    @JSONField(name = "ExitType")
    private String exitType;
    /**
     * 群组类型介绍，例如 Public
     */
    @JSONField(name = "Type")
    private GroupTypeEnum type;
    /**
     * 离开群的成员列表
     */
    @JSONField(name = "ExitMemberList")
    private List<MemberListDTO> memberList;

    @NoArgsConstructor
    @Data
    public static class MemberListDTO {
        @JSONField(name = "Member_Account")
        private String memberAccount;
    }
}

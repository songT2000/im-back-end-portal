package com.im.callback.entity;

import com.im.common.entity.enums.GroupTypeEnum;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 新成员入群之后回调
 */
@NoArgsConstructor
@Data
public class TiCbMemberJoinRequest extends TiCallbackRequest {

    private static final long serialVersionUID = 8582665463399795601L;
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
     * 入群方式：Apply（申请入群）；Invited（邀请入群）
     */
    @JSONField(name = "JoinType")
    private String joinType;
    /**
     * 群组类型介绍，例如 Public
     */
    @JSONField(name = "Type")
    private GroupTypeEnum type;
    /**
     * 新入群成员列表
     */
    @JSONField(name = "MemberList")
    private List<MemberListDTO> memberList;

    @NoArgsConstructor
    @Data
    public static class MemberListDTO {
        @JSONField(name = "Member_Account")
        private String memberAccount;
    }
}

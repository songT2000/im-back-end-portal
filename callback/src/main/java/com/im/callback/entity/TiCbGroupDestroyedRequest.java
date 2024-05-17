package com.im.callback.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.enums.GroupTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 群组解散之后回调
 */
@NoArgsConstructor
@Data
public class TiCbGroupDestroyedRequest extends TiCallbackRequest {

    private static final long serialVersionUID = -8947524212782812026L;
    /**
     * 操作的群 ID
     */
    @JSONField(name = "GroupId")
    private String groupId;
    /**
     * 群组名称
     */
    @JSONField(name = "Name")
    private String name;
    /**
     * 群主 UserID
     */
    @JSONField(name = "Owner_Account")
    private String ownerAccount;
    /**
     * 群组类型介绍，例如 Public
     */
    @JSONField(name = "Type")
    private GroupTypeEnum type;
    /**
     * 被解散的群组中的成员
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

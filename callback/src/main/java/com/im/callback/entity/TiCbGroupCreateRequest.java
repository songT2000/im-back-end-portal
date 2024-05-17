package com.im.callback.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.enums.GroupTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 创建群组之后回调
 */
@NoArgsConstructor
@Data
public class TiCbGroupCreateRequest extends TiCallbackRequest {

    private static final long serialVersionUID = -7553585580397585108L;
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
     * 请求创建的群组的名称
     */
    @JSONField(name = "Name")
    private String name;
    /**
     * 请求创建的群组的初始化成员列表
     */
    @JSONField(name = "MemberList")
    private List<MemberListDTO> memberList;
    /**
     * 用户建群时的自定义字段，这个字段默认是没有的，需要开通
     */
    @JSONField(name = "UserDefinedDataList")
    private List<UserDefinedDataListDTO> userDefinedDataList;

    @NoArgsConstructor
    @Data
    public static class MemberListDTO {
        @JSONField(name = "Member_Account")
        private String memberAccount;
    }

    @NoArgsConstructor
    @Data
    public static class UserDefinedDataListDTO {
        @JSONField(name = "Key")
        private String key;
        @JSONField(name = "Value")
        private String value;
    }
}

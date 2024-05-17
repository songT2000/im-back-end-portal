package com.im.common.util.api.im.tencent.entity.param.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.LongCodec;
import com.im.common.entity.enums.GroupMemberRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 设置成员角色
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiGroupMemberSetRoleParam implements Serializable {

    /**
     * 群组 ID
     */
    @JSONField(name = "GroupId")
    private String groupId;
    /**
     * 用户帐号
     */
    @JSONField(name = "Member_Account")
    private String account;
    /**
     * 角色，只能是管理员和普通成员
     */
    @JSONField(name = "Role")
    private GroupMemberRoleEnum role;
}

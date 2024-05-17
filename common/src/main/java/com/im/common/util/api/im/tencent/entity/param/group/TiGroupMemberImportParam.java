package com.im.common.util.api.im.tencent.entity.param.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.LongCodec;
import com.im.common.entity.enums.TiGroupRoleEnum;
import com.google.gson.annotations.SerializedName;
import com.im.common.entity.enums.GroupMemberRoleEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 导入群组参数
 */
@Data
@NoArgsConstructor
public class TiGroupMemberImportParam implements Serializable {

    /**
     * 群组ID(必填)
     */
    @JSONField(name = "GroupId")
    private String groupId;
    /**
     * 待添加的群成员数组
     */
    @JSONField(name = "MemberList")
    private List<Member> members;

    @NoArgsConstructor
    @Data
    public static class Member {
        /**
         * 待导入的群成员帐号(必填)
         */
        @JSONField(name = "Member_Account")
        private String memberAccount;
        /**
         * 待导入群成员角色；目前只支持填 Admin，不填则为普通成员 Member (选填)
         */
        @JSONField(name = "Role")
        private GroupMemberRoleEnum role;
        /**
         * 待导入群成员的入群时间(选填)
         */
        @JSONField(name = "JoinTime",serializeUsing = LongCodec.class)
        private Long joinTime;
        /**
         * 待导入群成员的未读消息计数(选填)
         */
        @JSONField(name = "UnreadMsgNum")
        private Integer unreadMsgNum;
    }
}

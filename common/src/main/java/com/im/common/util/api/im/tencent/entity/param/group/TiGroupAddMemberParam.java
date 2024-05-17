package com.im.common.util.api.im.tencent.entity.param.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 删除群成员
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiGroupAddMemberParam implements Serializable {

    public TiGroupAddMemberParam(String groupId, List<Member> members) {
        this.groupId = groupId;
        this.members = members;
    }

    /**
     * 要操作的群组（必填）
     */
    @JSONField(name = "GroupId")
    private String groupId;

    /**
     * 要新增的群成员列表，最多300个(必填)
     */
    @JSONField(name = "MemberList")
    private List<Member> members;
    /**
     * 是否静默加人。0表示非静默加人，1表示静默加人
     * <br>静默即增加成员时不通知群里所有成员。不填写该字段时默认为0
     */
    @JSONField(name = "Silence")
    private Integer silence;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Member{
        /**
         * 待导入的群成员帐号(必填)
         */
        @JSONField(name = "Member_Account")
        private String memberAccount;
    }

}

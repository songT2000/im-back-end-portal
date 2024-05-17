package com.im.common.util.api.im.tencent.entity.param.group;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 删除群成员
 */
@Data
@NoArgsConstructor
public class TiGroupDeleteMemberParam implements Serializable {

    /**
     * 要操作的群组（必填）
     */
    @JSONField(name = "GroupId")
    private String groupId;

    /**
     * 要删除的群成员列表，最多100个(必填)
     */
    @JSONField(name = "MemberToDel_Account")
    private List<String> accounts;
    /**
     * 是否静默删人。0表示非静默删人，1表示静默删人
     * <br>静默即删除成员时不通知群里所有成员，只通知被删除群成员。不填写该字段时默认为0
     */
    @JSONField(name = "Silence")
    private Integer silence;

    /**
     * 踢出用户原因
     */
    @JSONField(name = "Reason")
    private String reason;

    public TiGroupDeleteMemberParam(String groupId, List<String> accounts, String reason) {
        this.groupId = groupId;
        this.accounts = accounts;
        this.reason = reason;
    }
}

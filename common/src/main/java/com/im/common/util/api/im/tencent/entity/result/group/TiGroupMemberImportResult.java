package com.im.common.util.api.im.tencent.entity.result.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 导入群组返回结构
 */
@Data
@NoArgsConstructor
public class TiGroupMemberImportResult extends TiBaseResult {

    /**
     * 返回添加的群成员结果
     */
    @JSONField(name = "MemberList")
    private List<Member> members;

    @NoArgsConstructor
    @Data
    public static class Member {
        /**
         * 导入的群成员帐号
         */
        @JSONField(name = "Member_Account")
        private String memberAccount;
        /**
         * 导入结果：0表示失败；1表示成功；2表示已经是群成员
         */
        @JSONField(name = "Result")
        private Integer result;
    }
}

package com.im.common.util.api.im.tencent.entity.result.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 群组结构
 */
@Data
@NoArgsConstructor
public class TiGroupIdResult extends TiBaseResult {
    /**
     * App 当前的群组总数
     */
    @JSONField(name = "TotalCount")
    private Integer totalCount;
    /**
     * 获取到的群组 ID 的集合
     */
    @JSONField(name = "GroupIdList")
    private List<GroupIdListDTO> groupIdList;
    /**
     * 分页拉取的标志
     */
    @JSONField(name = "Next")
    private Long next;

    @NoArgsConstructor
    @Data
    public static class GroupIdListDTO {
        @JSONField(name = "GroupId")
        private String groupId;
    }
}

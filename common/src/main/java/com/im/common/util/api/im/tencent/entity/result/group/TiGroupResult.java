package com.im.common.util.api.im.tencent.entity.result.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 查询群组信息返回结构
 */
@Data
@NoArgsConstructor
public class TiGroupResult extends TiBaseResult {
    /**
     * 返回结果为群组信息数组
     */
    @JSONField(name = "GroupInfo")
    private List<TiGroup> groups;

}

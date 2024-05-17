package com.im.common.util.api.im.tencent.entity.result.operation;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 运营数据结果
 */
@Data
@NoArgsConstructor
public class TiAppOperationResult extends TiBaseResult {

    /**
     * 最近30天的运营数据
     */
    @JSONField(name = "Result")
    private List<TiAppOperationItem> results;

}

package com.im.common.util.api.im.tencent.entity.param.portrait;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 资料
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiAccountPortraitTagParam implements Serializable {
    /**
     * 指定要设置的资料字段的名称
     */
    @JSONField(name = "Tag")
    private TiAccountPortraitTagEnum tag;

    /**
     * 待设置的资料字段的值，详情可参见
     */
    @JSONField(name = "Value")
    private Object value;
}

package com.im.common.util.api.im.tencent.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 自定义字段
 */
@Data
@NoArgsConstructor
public class TiCustomItem implements Serializable {
    /**
     * 指定要设置的资料字段的名称，支持设置的资料字段有：
     * <br> 1. 标配资料字段，详情可参见 <a href="https://cloud.tencent.com/document/product/269/1500#.E6.A0.87.E9.85.8D.E8.B5.84.E6.96.99.E5.AD.97.E6.AE.B5>"标配资料字段</a>
     * <br> 2. 自定义资料字段，详情可参见 <a href="https://cloud.tencent.com/document/product/269/1500#.E8.87.AA.E5.AE.9A.E4.B9.89.E8.B5.84.E6.96.99.E5.AD.97.E6.AE.B5">自定义资料字段</a>
     */
    @JSONField(name = "Tag")
    private String tag;

    /**
     * 待设置的资料字段的值（string 或者 long类型 或者 array类型），详情可参见 <a href="https://cloud.tencent.com/document/product/269/1500#.E8.B5.84.E6.96.99.E5.AD.97.E6.AE.B5">资料字段</a>
     */
    @JSONField(name = "Value")
    private Object value;
}

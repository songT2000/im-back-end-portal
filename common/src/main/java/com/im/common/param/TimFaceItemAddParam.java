package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 新增表情包专辑参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimFaceItemAddParam {

    /**
     * 表情包专辑ID
     */
    @ApiModelProperty(value = "表情包专辑ID")
    private Long timFaceId;
    /**
     * 主图地址 240*240
     */
    @ApiModelProperty(value = "表情包地址，用于发送")
    private String faceUrl;
}

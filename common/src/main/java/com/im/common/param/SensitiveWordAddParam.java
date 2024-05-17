package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 新增敏感词参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class SensitiveWordAddParam {

    /**
     * 敏感词
     */
    @NotBlank
    @ApiModelProperty(value = "敏感词", required = true)
    private String words;

}

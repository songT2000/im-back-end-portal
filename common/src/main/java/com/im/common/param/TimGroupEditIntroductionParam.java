package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 修改群组简介
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupEditIntroductionParam {
    @NotBlank
    @ApiModelProperty(value = "群组ID", required = true, position = 1)
    private String groupId;

    @NotBlank
    @ApiModelProperty(value = "群组简介", required = true, position = 2)
    private String introduction;
}

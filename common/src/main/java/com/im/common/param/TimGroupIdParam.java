package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 群组ID参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupIdParam {
    @NotBlank
    @ApiModelProperty(value = "群组ID", required = true)
    private String groupId;

}

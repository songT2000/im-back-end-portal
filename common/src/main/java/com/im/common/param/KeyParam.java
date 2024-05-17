package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@ApiModel
public class KeyParam {
    @NotNull
    @ApiModelProperty(value = "key", required = true)
    private String key;
}

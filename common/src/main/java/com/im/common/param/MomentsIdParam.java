package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class MomentsIdParam {
    @NotNull
    @ApiModelProperty(value = "朋友圈ID", required = true)
    private Long momentsId;
}

package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 已读/未读
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class IdReadUnreadParam {
    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "已读/未读", required = true)
    private Boolean read;
}

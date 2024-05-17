package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class IdRemarkRequiredParam {
    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "备注", required = true)
    private String remark;
}

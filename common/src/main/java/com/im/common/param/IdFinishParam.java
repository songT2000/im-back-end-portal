package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class IdFinishParam {
    @NotNull
    @ApiModelProperty(value = "ID", required = true, position = 1)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "true是完成，false是失败", required = true, position = 2)
    private Boolean finish;

    @ApiModelProperty(value = "备注", position = 3)
    private String remark;
}

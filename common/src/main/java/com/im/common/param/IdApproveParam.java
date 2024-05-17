package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * ID审核参数
 *
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class IdApproveParam {
    @NotNull
    @ApiModelProperty(value = "数据ID", required = true, position = 1)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "true=通过，false=拒绝", required = true, position = 2)
    private Boolean approve;

    @ApiModelProperty(value = "备注", position = 3)
    private String remark;
}

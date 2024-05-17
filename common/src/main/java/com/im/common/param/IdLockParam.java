package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * ID锁定参数
 *
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class IdLockParam {
    @NotNull
    @ApiModelProperty(value = "数据ID", required = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "true=锁定，false=解锁", required = true)
    private Boolean lock;
}

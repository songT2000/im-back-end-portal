package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 设置用户全局禁言参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGlobalShutUpSetParam {

    @NotNull
    @ApiModelProperty(value = "用户ID", position = 1)
    private Long userId;

    @NotNull
    @ApiModelProperty(value = "单聊禁言秒数，永久禁言传4294967295，不禁言传0", position = 2)
    private Long c2cShutupEndTime;

    @NotNull
    @ApiModelProperty(value = "群聊禁言秒数，永久禁言传4294967295，不禁言传0", position = 3)
    private Long groupShutupEndTime;
}

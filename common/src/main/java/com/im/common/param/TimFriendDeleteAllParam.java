package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 删除用户所有的好友参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimFriendDeleteAllParam {

    @NotNull
    @ApiModelProperty(value = "用户ID", position = 1)
    private Long userId;
}

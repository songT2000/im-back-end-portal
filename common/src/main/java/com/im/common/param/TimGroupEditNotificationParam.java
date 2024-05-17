package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 修改群组公告
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupEditNotificationParam {
    @NotBlank
    @ApiModelProperty(value = "群组ID", required = true, position = 1)
    private String groupId;

    @NotBlank
    @ApiModelProperty(value = "群组公告", required = true, position = 2)
    private String notification;
}

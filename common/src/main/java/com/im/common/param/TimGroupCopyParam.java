package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 一键复制创建新群
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupCopyParam {
    @NotBlank
    @ApiModelProperty(value = "原群组ID", required = true, position = 1)
    private String groupId;

    @NotNull
    @ApiModelProperty(value = "新群的名称", required = true, position = 2)
    private String groupName;
}

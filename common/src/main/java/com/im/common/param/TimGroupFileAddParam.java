package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 保存群组简介
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupFileAddParam {

    @NotBlank
    @ApiModelProperty(value = "群组的ID",position = 1,required = true)
    private String groupId;

    @NotBlank
    @ApiModelProperty(value = "群文件地址",position = 2,required = true)
    private String url;

    @ApiModelProperty(value = "群文件大小，单位b",position = 3,required = true)
    private long size;

    @NotBlank
    @ApiModelProperty(value = "群文件名称",position = 4,required = true)
    private String fileName;

    @ApiModelProperty(value = "群文件后缀",position = 5)
    private String postfix;
}

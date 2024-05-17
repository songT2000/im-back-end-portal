package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 发送单聊文本消息
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimMessageC2cSendParam {
    @NotNull
    @ApiModelProperty(value = "接收用户的ID", required = true, position = 1)
    private Long toUserId;

    @NotBlank
    @ApiModelProperty(value = "消息内容", required = true, position = 2)
    private String content;

}

package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 单聊消息撤回请求
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimMessageC2cWithdrawParam {
    @NotNull
    @ApiModelProperty(value = "消息发送人ID", required = true, position = 1)
    private Long fromUserId;

    @NotNull
    @ApiModelProperty(value = "消息接收人ID", required = true, position = 2)
    private Long toUserId;

    @NotBlank
    @ApiModelProperty(value = "撤回的消息标识符", required = true, position = 3)
    private String msgKey;

}

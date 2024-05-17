package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * app扫码加群参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupMemberJoinByQrcodeParam {
    @NotBlank
    @ApiModelProperty(value = "群组ID", required = true, position = 1)
    private String groupId;

    @NotBlank
    @ApiModelProperty(value = "二维码分享用户的账号", required = true, position = 2)
    private String username;

    @ApiModelProperty(value = "二维码生成的时间戳", position = 3)
    private Long time;

}

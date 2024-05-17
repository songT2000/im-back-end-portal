package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 设置/取消群组管理员参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupManagerParam {
    @NotBlank
    @ApiModelProperty(value = "群组ID", required = true, position = 1)
    private String groupId;

    @NotNull
    @ApiModelProperty(value = "管理员ID", required = true, position = 2)
    private Long userId;

    @NotNull
    @ApiModelProperty(value = "true=设置管理员，false=取消管理员", required = true, position = 3)
    private Boolean enable;

}

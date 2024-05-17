package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 群组全员禁言 启/禁
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupShutUpParam {
    @NotBlank
    @ApiModelProperty(value = "群组ID", required = true, position = 1)
    private String groupId;

    @NotNull
    @ApiModelProperty(value = "true=禁言，false=不禁言", required = true, position = 2)
    private Boolean enable;
}

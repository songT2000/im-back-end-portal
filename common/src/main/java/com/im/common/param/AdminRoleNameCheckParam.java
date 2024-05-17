package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 只有一个角色名的接口
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class AdminRoleNameCheckParam {
    @NotBlank
    @ApiModelProperty(value = "角色名", required = true)
    private String name;

    /**
     * 不等于此ID
     **/
    @ApiModelProperty(value = "不等于此ID")
    private Long notId;
}

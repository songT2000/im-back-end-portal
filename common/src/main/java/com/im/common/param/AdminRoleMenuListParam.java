package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 角色菜单权限列表
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class AdminRoleMenuListParam {
    /**
     * 角色ID
     **/
    @NotNull
    @ApiModelProperty(value = "角色ID", required = true)
    private Long id;
}

package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 编辑下级角色权限
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class AdminRoleEditMenuParam {
    @NotNull
    @ApiModelProperty(value = "角色ID", required = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "菜单ID数组", required = true)
    private Set<Long> menus;
}

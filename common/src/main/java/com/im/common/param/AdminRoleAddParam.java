package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加下级角色
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class AdminRoleAddParam {
    @NotBlank
    @ApiModelProperty(value = "角色名", required = true)
    private String name;

    @NotNull
    @ApiModelProperty(value = "父角色ID", required = true)
    private Long parentId;

    @NotNull
    @ApiModelProperty(value = "排序号", required = true)
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;
}

package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 编辑菜单
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class AdminMenuEditParam {
    @NotNull
    @ApiModelProperty(value = "菜单ID", required = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @ApiModelProperty(value = "图标，仅类型=菜单时需要填写")
    private String icon;
}

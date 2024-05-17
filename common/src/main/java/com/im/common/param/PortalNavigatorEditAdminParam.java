package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 编辑前台导航
 *
 * @author Barry
 * @date 2022-03-25
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalNavigatorEditAdminParam {
    @NotNull
    @ApiModelProperty(value = "ID", required = true, position = 1)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "名称", required = true, position = 2)
    private String name;

    @NotBlank
    @ApiModelProperty(value = "链接", required = true, position = 3)
    private String url;

    @NotNull
    @ApiModelProperty(value = "排序号", required = true, position = 4)
    private Integer sort;

    @NotNull
    @ApiModelProperty(value = "是否启用", required = true, position = 5)
    private Boolean enabled;
}

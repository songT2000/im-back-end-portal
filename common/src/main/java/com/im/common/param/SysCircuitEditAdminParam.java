package com.im.common.param;

import com.im.common.entity.enums.SysCircuitTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 修改资讯
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class SysCircuitEditAdminParam {
    @NotNull
    @ApiModelProperty(value = "ID", required = true, position = 1)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "线路测速地址", required = true, position = 2)
    private String circuitUrl;

    @NotNull
    @ApiModelProperty(value = "类型", required = true, position = 3)
    private SysCircuitTypeEnum circuitType;

    @ApiModelProperty(value = "备注", position = 4)
    private String remark;

    @NotNull
    @ApiModelProperty(value = "是否启用", required = true, position = 5)
    private Boolean enabled;
}

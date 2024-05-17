package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 编辑银行
 *
 * @author Barry
 * @date 2019-11-20
 */
@Data
@NoArgsConstructor
@ApiModel
public class BankEditAdminParam {
    @NotNull
    @ApiModelProperty(value = "ID", required = true, position = 1)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "名称", required = true, position = 2)
    private String name;

    @NotNull
    @ApiModelProperty(value = "排序号", required = true, position = 3)
    private Integer sort;

    @NotNull
    @ApiModelProperty(value = "是否启用提现", position = 4)
    private Boolean withdrawEnabled;
}

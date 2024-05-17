package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 上下线
 *
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class OnlineOfflineParam {
    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "true是上线，false是下线", required = true)
    private Boolean online;

    @ApiModelProperty(value = "备注")
    private String remark;
}

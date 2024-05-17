package com.im.common.param;

import com.im.common.entity.enums.BlackWhiteTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 区域黑白名单
 *
 * @author Barry
 * @date 2021-02-27
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalAreaBlackWhiteEditAdminParam {
    @NotNull
    @ApiModelProperty(value = "ID", required = true, position = 1)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "区域，模糊匹配，只要区域中包含字样就成立", required = true, position = 2)
    private String area;

    @NotNull
    @ApiModelProperty(value = "黑白名单", required = true, position = 3)
    private BlackWhiteTypeEnum blackWhite;

    @ApiModelProperty(value = "备注", position = 4)
    private String remark;
}

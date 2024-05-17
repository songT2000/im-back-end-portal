package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 公告列表
 *
 * @author max
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalSysNoticeListParam {
    @NotBlank
    @ApiModelProperty(value = "语言编码", required = true, position = 7)
    private String languageCode;
}

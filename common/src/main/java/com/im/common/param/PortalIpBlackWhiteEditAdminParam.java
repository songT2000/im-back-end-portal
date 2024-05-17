package com.im.common.param;

import com.im.common.entity.enums.BlackWhiteTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加用户IP黑白名单
 *
 * @author Max
 * @date 2021-02-21
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalIpBlackWhiteEditAdminParam {
    @NotNull
    @ApiModelProperty(value = "ID", required = true, position = 1)
    private Long id;

    @ApiModelProperty(value = "用户名列表，多个用英文逗号分割，没有就是全局", position = 2)
    private String usernames;

    @NotBlank
    @ApiModelProperty(value = "IP", required = true, position = 3)
    private String ip;

    @NotNull
    @ApiModelProperty(value = "黑白名单", required = true, position = 4)
    private BlackWhiteTypeEnum blackWhite;

    @ApiModelProperty(value = "备注", position = 5)
    private String remark;
}
